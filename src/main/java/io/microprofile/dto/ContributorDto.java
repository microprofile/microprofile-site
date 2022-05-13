package io.microprofile.dto;

import java.util.Objects;

public class ContributorDto {
    private String company;
    private int contributions;
    private String location;
    private String login;
    private String name;


    public ContributorDto(String company, int contributions, String location, String login, String name) {
        this.company = company;
        this.contributions = contributions;
        this.location = location;
        this.login = login;
        this.name = name;
    }


    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public int getContributions() {
        return contributions;
    }

    public void setContributions(int contributions) {
        this.contributions = contributions;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void increaseContributions(int newContributions) {
        this.contributions = this.contributions + newContributions;
    }

    @Override
    public String toString() {
        return "ContributorDto{" + "company='" + company + '\'' + ", contributions=" + contributions + ", location='" +
                location + '\'' + ", login='" + login + '\'' + ", name='" + name + '\'' + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContributorDto that = (ContributorDto) o;
        return Objects.equals(company, that.company) && Objects.equals(location, that.location) &&
                login.equals(that.login) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(company, location, login, name);
    }
}
