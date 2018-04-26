package cn.yejy.data;

import org.springframework.http.ResponseEntity;

public class ResponseData {

    private static class Base {
        private Integer code; // 成功为0，其它为错误码
        private String msg;
        private Object data;

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public Object getData() {
            return data;
        }

        public void setData(Object data) {
            this.data = data;
        }

        @Override
        public String toString() {
            return "Base{" +
                    "code=" + code +
                    ", msg='" + msg + '\'' +
                    ", data=" + data +
                    '}';
        }
    }

    private static class Error {
        private Integer code;
        private String msg;

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        @Override
        public String toString() {
            return "Error{" +
                    "code=" + code +
                    ", msg='" + msg + '\'' +
                    '}';
        }
    }

    public static <T> ResponseEntity ok(String msg, T t) {
        Base base = new Base();
        base.setCode(0);
        base.setMsg(msg);
        base.setData(t);
        return ResponseEntity.ok(base);
    }

    public static ResponseEntity ok(String msg) {
        Error base = new Error();
        base.setCode(0);
        base.setMsg(msg);
        return ResponseEntity.ok(base);
    }

    public static ResponseEntity error(Integer errorCode, String msg) {
        Error error = new Error();
        error.setCode(errorCode);
        error.setMsg(msg);
        return ResponseEntity.ok(error);
    }
}
