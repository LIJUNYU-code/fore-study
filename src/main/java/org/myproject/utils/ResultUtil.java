package org.myproject.utils;

import org.myproject.vo.ResultVO;

public class ResultUtil {

    // TODO: 可根据需要扩展更多快捷方法
    // - successWithMessage(data, message)
    // - error404(message)
    // - error403(message)
    // - error400(message)

    public static <T> ResultVO<T> success(T data) {
        return ResultVO.success(data);
    }

    public static <T> ResultVO<T> success() {
        return ResultVO.success();
    }

    public static <T> ResultVO<T> error(Integer code, String message) {
        return ResultVO.error(code, message);
    }

    public static <T> ResultVO<T> error(String message) {
        return ResultVO.error(message);
    }
}