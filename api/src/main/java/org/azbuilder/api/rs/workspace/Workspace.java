package org.azbuilder.api.rs.workspace;

import com.yahoo.elide.annotation.*;
import lombok.Getter;
import lombok.Setter;
import org.azbuilder.api.rs.Organization;
import org.azbuilder.api.rs.vcs.Vcs;
import org.azbuilder.api.rs.workspace.parameters.Variable;
import org.azbuilder.api.rs.job.Job;
import org.azbuilder.api.rs.workspace.history.History;
import org.azbuilder.api.rs.workspace.schedule.Schedule;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;


@ReadPermission(expression = "team view workspace")
@CreatePermission(expression = "team manage workspace")
@UpdatePermission(expression = "team manage workspace")
@DeletePermission(expression = "team manage workspace")
@Include
@Getter
@Setter
@Entity
public class Workspace {

    @Id
    @Type(type="uuid-char")
    @GeneratedValue
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "source")
    private String source;

    @Column(name = "branch")
    private String branch;

    @Column(name = "terraform_version")
    private String terraformVersion;

    @ManyToOne
    private Organization organization;

    @OneToMany(mappedBy = "workspace")
    private List<Variable> variable;

    @UpdatePermission(expression = "user is a super service")
    @OneToMany(mappedBy = "workspace")
    private List<History> history;

    @OneToMany(mappedBy = "workspace")
    private List<Schedule> schedule;

    @OneToMany(mappedBy = "workspace")
    private List<Job> job;

    @OneToOne
    private Vcs vcs;
}
