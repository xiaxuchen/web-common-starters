package org.originit.crud.pojo;

import lombok.Data;
import org.originit.crud.anno.Column;
import org.originit.crud.anno.Id;
import org.originit.crud.anno.LogicDelete;
import org.originit.crud.anno.TableName;

import java.util.Objects;

@TableName("t_user")
public class User {

    @Id
    private Long id;

    private String name;

    private Integer age;

    @LogicDelete
    @Column("is_del")
    private Boolean isDel;

    public User() {
    }

    public User(Long id) {
        this.id = id;
    }

    public User(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public User(Long id, String name, Integer age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Boolean getIsDel() {
        return isDel;
    }

    public void setIsDel(Boolean isDel) {
        this.isDel = isDel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(name, user.name) && Objects.equals(age, user.age) && Objects.equals(isDel, user.isDel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, age, isDel);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", isDel='" + isDel + '\'' +
                '}';
    }
}
