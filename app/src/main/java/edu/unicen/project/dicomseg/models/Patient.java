package edu.unicen.project.dicomseg.models;

public class Patient {

    private String id;
    private String name;
    private String sex;
    private Integer age;
    private String birthDate;
    private Double weight;
    private String address;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(String age) {
        try {
            this.age = Integer.valueOf(age);
        } catch (NumberFormatException e) {
            // Age format: e.g. 052Y
            String onlyNumbers = age.replaceAll("\\D+","");
            this.age = Integer.valueOf(onlyNumbers);
        }
    }

}
