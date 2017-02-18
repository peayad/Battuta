package iti_edu.battuta;

public class Trip {

    private int id;
    private String title;
    private String startPoint;
    private String endPoint;
    private String date;
    private String time;
    private int isRound;
    private int isDone;
    private String notes;

    public Trip(int id, String title, String startPoint, String endPoint, String date,String time, int isRound, int isDone, String notes)
    {
        this.id = id;
        this.title = title;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.date = date;
        this.time = time;
        this.isRound = isRound;
        this.isDone = isDone;
        this.notes = notes;
    }

    public Trip(int id, String title, String startPoint, String endPoint){
        this.id = id;
        this.title = title;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
    }

    public void setId(int id){
        this.id = id;
    }
    public int getId(){
        return id;
    }

    public void setTitle(String title){
        this.title = title;
    }
    public String getTitle(){
        return title;
    }

    public void setStartPoint(String startPoint){
        this.startPoint = startPoint;
    }
    public String getStartPoint(){
        return startPoint;
    }

    public void setEndPoint(String endPoint){
        this.endPoint = endPoint;
    }
    public String getEndPoint(){
        return endPoint;
    }

    public void setDate(String date){
        this.date = date;
    }
    public String getDate(){
        return date;
    }

    public void setTime(String time){
        this.time = time;
    }
    public String getTime(){
        return time;
    }

    public void setIsround(int isRound){
        this.isRound = isRound;
    }
    public int getIsRound(){
        return isRound;
    }

    public void setIsDone(int isDone){
        this.isDone = isDone;
    }
    public int getIsDone(){
        return isDone;
    }

    public void setNotes(String notes){
        this.notes = notes;
    }
    public String getNotes(){
        return notes;
    }


}
