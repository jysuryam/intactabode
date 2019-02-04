package javaapplication3;


public class Employee {
    private String name;
    private String dept;
    private String uid;
    private String address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }


    public String getUid() {
        return uid;
    }

    /**
     * @param uid the uid to set
     */
    public void setUid(String uid) {
        this.uid = uid;
    }
//    public void setPhone(String phone) {
//        this.phone = phone;
//    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "name='" + name + '\'' +
                ", dept='" + dept + '\'' +
                ", phone='" + uid+ '\'' +
                ", address='" + address + '\'' +
                '}';
    }

    /**
     * @return the uid
     */

}