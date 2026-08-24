package it.unipi.client.util;

import javafx.scene.control.Label;


public class MessageHandler {
    
    private Label messageLabel;
    private String successStyle;
    private String errorStyle;

    public MessageHandler(Label messageLabel, String successStyle, String errorStyle) {
        this.messageLabel = messageLabel;
        this.successStyle = successStyle;
        this.errorStyle = errorStyle;
    }
    
    public void showMessage(String msg, boolean isError){
        
        String styleClass = (isError)? errorStyle : successStyle;
        
        hideMessage();
        
        messageLabel.setText(msg);
        messageLabel.getStyleClass().add(styleClass);
        messageLabel.setVisible(true);
    }
    
    
    public void hideMessage(){
        messageLabel.setText("");
        messageLabel.getStyleClass().removeAll(successStyle, errorStyle);
        messageLabel.setVisible(false);
    }
    
}
