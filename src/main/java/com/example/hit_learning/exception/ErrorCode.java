package com.example.hit_learning.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ErrorCode {
    INTERNAL_ERROR(9999,"SOMETHING GONE WRONG!",HttpStatus.INTERNAL_SERVER_ERROR),


    USER_EXISTED(1001,"Người dùng đã tồn tại", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1002,"Người dùng không tồn tại",HttpStatus.NOT_FOUND) ,
    USER_UNAUTHENTICATED(1005,"Sai tên đăng nhập hoặc mật khẩu!",HttpStatus.UNAUTHORIZED),
    USER_LOGGED_OUT(1007,"Đăng xuất thành công",HttpStatus.UNAUTHORIZED),
    USER_TOKEN_INCORRECT(403, "Phiên đã hết hạn vui lòng đăng nhập lại",  HttpStatus.FORBIDDEN),


    COURSE_NOT_EXISTED(1002, "Khoá học không tồn tại", HttpStatus.NOT_FOUND) ,
    COURSE_FILE_LONG(1002, "File không được quá 50MB", HttpStatus.BAD_REQUEST),

    SECTION_NOT_EXISTED(1002, "Thư mục không tồn tại", HttpStatus.NOT_FOUND) ,

    ITEM_NOT_EXISTED(1009, "Bài đăng không tồn tại", HttpStatus.NOT_FOUND),
    ITEM_FILE_LONG(1002, "File không được quá 1GB", HttpStatus.BAD_REQUEST),

    COMMENT_NOT_EXISTED(1011, "Bình luận không tồn tại", HttpStatus.NOT_FOUND),

    MINIO_NOT_FOUND(1002,"Không tìm thấy tệp tin",HttpStatus.NOT_FOUND),

    EMAIL_NOT_FOUND(404, "Địa chỉ eamil không hợp lệ", HttpStatus.NOT_FOUND),
    ;

    private int code;
    private String message;
    private HttpStatus httpStatus;

}
