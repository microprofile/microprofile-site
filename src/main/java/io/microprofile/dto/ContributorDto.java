/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package io.microprofile.dto;

import java.util.Objects;

public class ContributorDto {
    private String company;
    private int contributions;
    private String location;
    private String login;
    private String name;
    private String avatar;
    private String profile;


    public ContributorDto(String company, int contributions, String location, String login, String name, String avatar, String profile) {
        this.company = company;
        this.contributions = contributions;
        this.location = location;
        this.login = login;
        this.name = name;
        this.avatar = avatar;
        this.profile = profile;
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

    public void setProfile(String profile) {
        this.profile = profile;
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
