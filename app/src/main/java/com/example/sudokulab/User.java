package com.example.sudokulab;

class User {
    public String name;
    public String email;
    public String age;
    public long score;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setScore(long score) { this.score = score;}

    public long getScore() {return score;}

    public User() {
    }
}
