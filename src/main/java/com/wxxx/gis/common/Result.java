package com.wxxx.gis.common;


public class Result {
    private Integer code = 0;
    private String msg = "";
    private Object data = null;

    public Result() {
    }

    public Result(Integer code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    /**
     * 功能描述: 通用成功返回值（无data对象）
     *
     * @param:
     * @return:
     * @author: wang jun
     * @date: 2022/1/10 14:32
     */
    public Result success() {
        return new Result(Constants.REQUEST_SUCCESS, Constants.RETURN_SUCCESS, null);
    }

    /**
     * 功能描述: 通用成功返回值（有data对象）
     *
     * @param:
     * @return:
     * @author: wang jun
     * @date: 2022/1/10 14:32
     */
    public Result success(Object o) {
        return new Result(Constants.REQUEST_SUCCESS, Constants.RETURN_SUCCESS, o);
    }

    /**
     * 功能描述: 通用失败返回值（无data对象）
     *
     * @param:
     * @return:
     * @author: wang jun
     * @date: 2022/1/10 14:32
     */
    public Result failed(String msg) {
        return new Result(Constants.REQUEST_FAILED, msg, null);
    }

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
        return "Result{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
