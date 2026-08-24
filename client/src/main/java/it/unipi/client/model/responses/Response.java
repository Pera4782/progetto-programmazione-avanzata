package it.unipi.client.model.responses;


public class Response {
    
    private enum Status { SUCCESS, ERROR }
    
    private Status status;

    public Response(Boolean isError) {
        status = (isError)? Status.ERROR : Status.SUCCESS;
    }
    
    public boolean isError(){
        return status == Status.ERROR;
    }
}
