package cn.yzlee.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Version;
import java.time.LocalDateTime;

/**
 * @Author:lyz
 * @Date: 2018/3/22 15:43
 * @Desc:
 **/
@Entity
public class SuperObject
{
    @Column(name = "create_time")
    private LocalDateTime createTime;

    @Column(name = "update_time")
    private LocalDateTime updateTime;

    @Version
    @Column(name="version")
    private long version;

}
