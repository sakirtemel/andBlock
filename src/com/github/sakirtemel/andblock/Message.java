package com.github.sakirtemel.andblock;

public class Message {
	String _number;
	String _message;
	String _date;
	
	public Message(){
		//
    }
	
	public String getNumber(){
        return this._number;
    }

    public void setNumber(String number){
        this._number = number;
    }
    public String getMessage(){
        return this._message;
    }

    public void setMessage(String message){
        this._message = message;
    }
    public String getDate(){
        return this._date;
    }

    public void setDate(String date){
        this._date = date;
    }
    
}

