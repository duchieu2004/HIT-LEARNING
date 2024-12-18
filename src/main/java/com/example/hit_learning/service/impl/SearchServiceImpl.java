package com.example.hit_learning.service.impl;

import com.example.hit_learning.dto.request.PageRequest;
import com.example.hit_learning.dto.response.*;
import com.example.hit_learning.entity.Course;
import com.example.hit_learning.entity.Item;
import com.example.hit_learning.entity.Section;
import com.example.hit_learning.entity.User;
import com.example.hit_learning.mapper.CourseMapper;
import com.example.hit_learning.mapper.ItemMapper;
import com.example.hit_learning.mapper.SectionMapper;
import com.example.hit_learning.mapper.UserMapper;
import com.example.hit_learning.repository.CourseRepository;
import com.example.hit_learning.repository.UserRepository;
import com.example.hit_learning.service.SearchService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.hibernate.search.backend.elasticsearch.ElasticsearchExtension;
import org.hibernate.search.engine.search.query.SearchQuery;
import org.hibernate.search.engine.search.query.SearchResult;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.massindexing.MassIndexer;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SearchServiceImpl implements SearchService {
    @PersistenceContext
    EntityManager entityManager;
    final CourseMapper courseMapper;
    final SectionMapper sectionMapper;
    final ItemMapper itemMapper;
    final UserMapper userMapper;

    final UserRepository userRepository;
    final CourseRepository courseRepository;

    final String[] FIELDS = {"name", "description"};
    final static int PAGE_SIZE=15;
    final int MAX_EDIT_DISTANCE= 1;
    final int MAX_EDIT_WORD= 1;
    @Override
    @SneakyThrows
    public List<CourseResponse> findCourseByName(String courseName) {

        MassIndexer indexer = Search.session(entityManager).massIndexer(Course.class);
        indexer.startAndWait();

        SearchQuery<Course> searchQuery= this.getSearchQuery(Course.class, courseName);
        SearchResult<Course> searchResult= searchQuery.extension(ElasticsearchExtension.get())
                .fetch(PAGE_SIZE);

        List<CourseResponse> responses = new ArrayList<>();
        for(Course x : searchResult.hits()){
            var var = courseMapper.toResponse(x);
            responses.add(var);
        }
        return responses;
    }

    @Override
    @SneakyThrows
    public List<SectionResponse> findSectionByName(String sectionName) {
        MassIndexer indexer = Search.session(entityManager).massIndexer(Section.class);
        indexer.startAndWait();

        SearchQuery<Section> searchQuery= this.getSearchQuery(Section.class, sectionName);
        SearchResult<Section> searchResult= searchQuery.extension(ElasticsearchExtension.get())
                .fetch(PAGE_SIZE);

        List<SectionResponse> responses = new ArrayList<>();
        for(Section x : searchResult.hits()){
            var var = sectionMapper.toResponse(x);
            responses.add(var);
        }
        return responses;
    }

    @Override
    @SneakyThrows
    public List<ItemResponse> findIemByName(String itemName) {
        MassIndexer indexer = Search.session(entityManager).massIndexer(Item.class);
        indexer.startAndWait();

        SearchQuery<Item> searchQuery= this.getSearchQuery(Item.class, itemName);
        SearchResult<Item> searchResult= searchQuery.extension(ElasticsearchExtension.get())
                .fetch(PAGE_SIZE);

        List<ItemResponse> responses = new ArrayList<>();
        for(Item x : searchResult.hits()){
            var var = itemMapper.toResponse(x);
            responses.add(var);
        }
        return responses;
    }

    private <T>SearchQuery getSearchQuery(Class<T> entity, String term ){
        String regex = "^\".+\"$"; // dau \ de java co the lien ket doan nay
        Pattern pattern = Pattern.compile(regex) ;
        if(pattern.matcher(term.trim()).matches()) {
            term = term.substring(1 , term.length()-1) ;
            return getSearchQueryPhrase(entity, term);
        }
        else{
            return getSearchQueryMatch(entity, term);
        }
    }

    private <T>SearchQuery getSearchQueryMatch(Class<T> entity, String term){
        return Search.session(entityManager)
                .search(entity)

                .where(t -> t.match()
                        .fields(FIELDS)
                        .matching(term)
                        .fuzzy(MAX_EDIT_DISTANCE))
                .highlighter(t-> t.plain().tag("<br>" , "</br>"))
                .toQuery();

    }
    private <T>SearchQuery<T> getSearchQueryPhrase(Class<T> entity, String term){
        return Search.session(entityManager)
                .search(entity)
                .where(t -> t.phrase()
                        .fields(FIELDS)
                        .matching(term)
                        .slop(MAX_EDIT_WORD))
                .toQuery();
    }

    @Override
    public <T> List<String> highLightByTerm(Class<T> entity, String terms)  {
        SearchSession searchSession = Search.session( entityManager );
        List<List<?>> result =  searchSession.search(entity)
                .extension(ElasticsearchExtension.get())
                .select(t -> t.composite().from(
                        t.highlight("name") ,
                        t.highlight("description")
                ).asList())
                .where( f -> f.phrase()
                        .fields(FIELDS)
                        .matching(terms)
                        .slop(2))
                .highlighter( t -> t.plain().tag("<br>" , "</br>"))
                .fetch(5).hits();
        return this.convertToListString(result) ;
    }

    private List<String> convertToListString(List<List<?>> result){
        List<String> ans = new ArrayList<>() ;
        for(List<?> x : result){
            for(Object y : x){
                String z = y + "" ;
                z = z.substring(1, z.length() - 1);
                if(z.length() != 0 && !ans.contains(z)) {
                    ans.add(z);
                }
            }
        }
        return ans ;
    }

    @Override
    public PageResponse<UserResponse> filterUserByName(String name, PageRequest request) {
        List<UserResponse> userResponses = new ArrayList<>();
        Pageable pageable = request.getPageable();
        Page<User> page = userRepository.findAllByNameContainingIgnoreCase(name, pageable);
        for(User x : page.getContent()){
            var response = userMapper.toResponse(x);
            userResponses.add(response);
        }
        PageResponse<UserResponse> response = new PageResponse<>(request.getPageIndex(), request.getPageSize(), request.getOrders(), userResponses, page.getTotalPages());
        return response;
    }

    @Override
    public PageResponse<CourseResponse> filterCourseByName(String name, PageRequest request) {
        List<CourseResponse> userResponses = new ArrayList<>();
        Pageable pageable = request.getPageable();
        Page<Course> page = courseRepository.findAllByNameContainingIgnoreCase(name, pageable);
        for(Course x : page.getContent()){
            var response = courseMapper.toResponse(x);
            userResponses.add(response);
        }
        PageResponse<CourseResponse> response = new PageResponse<>(request.getPageIndex(), request.getPageSize(), request.getOrders(), userResponses, page.getTotalPages());
        return response;
    }
}
