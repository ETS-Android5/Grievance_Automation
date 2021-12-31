package org.tensorflow.lite.examples.classification;

public class modelgrievances {
    String Number;
    String Lat;
    String Long;
    String Classifier;
    String Name;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public modelgrievances(String number, String lat, String aLong, String classifier, String name) {
        Number = number;
        Lat = lat;
        Long = aLong;
        Classifier = classifier;
        Name = name;
    }

    public modelgrievances() {
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
    }

    public String getLat() {
        return Lat;
    }

    public void setLat(String lat) {
        Lat = lat;
    }

    public String getLong() {
        return Long;
    }

    public void setLong(String aLong) {
        Long = aLong;
    }

    public String getClassifier() {
        return Classifier;
    }

    public void setClassifier(String classifier) {
        Classifier = classifier;
    }
}
