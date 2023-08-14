package org.originit.et.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 小程序用户表
 * </p>
 *
 * @author macro
 * @since 2023-03-29
 */
@Getter
@Setter
@Table(name = "app_user")
public class AppUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String nickname;

    private String unionId;

    private String wxOpenId;

    private String phone;

    private Integer schoolId;

    private LocalDateTime createTime;


}
