package org.myproject.config;

import lombok.extern.slf4j.Slf4j;
import org.myproject.vo.ResultVO;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // TODO: 完善异常处理, 可根据业务需要添加更多异常处理器

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResultVO<Void> handleValidationException(MethodArgumentNotValidException e) {
        // TODO: 提取校验错误信息, 返回400错误
        // e.getBindingResult().getFieldErrors() -> 获取所有字段错误
        // 拼接错误信息: "字段名: 错误消息"
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .reduce((a, b) -> a + "; " + b)
                .orElse("参数校验失败");
        return ResultVO.error(400, message);
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResultVO<Void> handleBindException(BindException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .reduce((a, b) -> a + "; " + b)
                .orElse("参数绑定失败");
        return ResultVO.error(400, message);
    }

    @ExceptionHandler(UnsupportedOperationException.class)
    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    public ResultVO<Void> handleTodoException(UnsupportedOperationException e) {
        // TODO标记的方法被调用时返回此响应
        return ResultVO.error(501, "功能待实现: " + e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResultVO<Void> handleRuntimeException(RuntimeException e) {
        // TODO: 记录错误日志
        // log.error("业务异常: ", e)
        // 可根据不同异常类型返回不同错误码
        log.error("业务异常: ", e);
        return ResultVO.error(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResultVO<Void> handleException(Exception e) {
        log.error("系统异常: ", e);
        return ResultVO.error("系统内部错误");
    }
}