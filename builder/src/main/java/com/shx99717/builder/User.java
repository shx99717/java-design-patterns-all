package com.shx99717.builder;

public class User {
    // Required
    private final String firstName;
    // Required
    private final String lastname;
    // Required
    private final int age;
    // Optional
    private final String phone;
    // Optional
    private final String address;

    private User(Builder builder) {
        this.firstName = builder.firstName;
        this.lastname = builder.lastName;
        this.age = builder.age;
        this.phone = builder.phone;
        this.address = builder.address;
    }

    public static class  Builder {
        private String firstName;
        private String lastName;
        private int age;
        private String phone;
        private String address;

        public Builder() {}

        // or starts with 'with' e.g. withFirstName
        public Builder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        // or starts with 'with' e.g. withLastName
        public Builder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder age(int age) {
            this.age = age;
            return this;
        }

        public Builder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public Builder address(String address) {
            this.address = address;
            return this;
        }

        public User build() {
            if(firstName == null) {
                throw new IllegalStateException("No first name given");
            }

            if(lastName == null) {
                throw new IllegalStateException("No last name given");
            }

            if(age == 0) {
                throw new IllegalStateException("No age given");
            }
           return new User(this);
        }
    }


    /////////////////////////////////////////////////////
    // Only getter, NO setter to provide immutability
    /////////////////////////////////////////////////////
    public String getFirstName() {
        return firstName;
    }

    public String getLastname() {
        return lastname;
    }

    public int getAge() {
        return age;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", lastname='" + lastname + '\'' +
                ", age=" + age +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
