package io.microprofile.dto;

import java.util.Objects;
import java.net.URL;

public class ContributorDto {
    private String company;
    private int contributions;
    private String location;
    private String login;
    private String name;
    private String avatar;
    private String profile;


    public ContributorDto(String company, int contributions, String location, String login, String name, String avatar, URL profile) {
        this.company = company;
        this.contributions = contributions;
        this.location = location;
        this.login = login;
        this.name = name;
        this.avatar = avatar;
        this.profile = profile.toString();
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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(URL profile) {
        this.profile = profile.toString();
    }

    public void increaseContributions(int newContributions) {
        this.contributions = this.contributions + newContributions;
    }

    @Override
    public String toString() {
        return "ContributorDto{" + "company='" + company + '\'' + ", contributions=" + contributions + ", location='" +
                location + '\'' + ", login='" + login + '\'' + ", name='" + name + '\'' + ", avatar='" + 
                avatar + '\'' + ", profile='" + profile + '\'' + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContributorDto that = (ContributorDto) o;
        return Objects.equals(company, that.company) && Objects.equals(location, that.location) &&
                login.equals(that.login) && Objects.equals(name, that.name) && Objects.equals(avatar, that.avatar) && Objects.equals(profile, that.profile);
    }

    @Override
    public int hashCode() {
        return Objects.hash(company, location, login, name, avatar, profile);
    }
}
