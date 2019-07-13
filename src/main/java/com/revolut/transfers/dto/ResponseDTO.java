package com.revolut.transfers.dto;

public class ResponseDTO<T> {

    private String message;
    private int status;
    private  T result;


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public static <T> ResponseDTO<T> createResponse(T resultObject , String message , int status){

       ResponseDTO<T> responseDTO = new ResponseDTO<>();
       responseDTO.setMessage(message);
       responseDTO.setStatus(status);
       responseDTO.setResult(resultObject);
       return responseDTO;
    }

}

