package org.azbuilder.api.rs.job;

import com.yahoo.elide.annotation.CreatePermission;
import com.yahoo.elide.annotation.Include;
import com.yahoo.elide.annotation.LifeCycleHookBinding;
import com.yahoo.elide.annotation.UpdatePermission;
import lombok.Getter;
import lombok.Setter;
import org.azbuilder.api.plugin.security.audit.GenericAuditFields;
import org.azbuilder.api.rs.Organization;
import org.azbuilder.api.rs.hooks.job.JobManageHook;
import org.azbuilder.api.rs.job.step.Step;
import org.azbuilder.api.rs.workspace.Workspace;

import javax.persistence.*;
import java.util.List;

@LifeCycleHookBinding(operation = LifeCycleHookBinding.Operation.CREATE, phase = LifeCycleHookBinding.TransactionPhase.POSTCOMMIT, hook = JobManageHook.class)
@Include(rootLevel = false)
@Getter
@Setter
@Entity
public class Job extends GenericAuditFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @UpdatePermission(expression = "team approve job OR user is a super service")
    @Enumerated(EnumType.STRING)
    private JobStatus status = JobStatus.pending;

    @Column(name = "output")
    private String output;

    @Column(name = "terraform_plan")
    private String terraformPlan;

    @CreatePermission(expression = "user is a super service")
    @UpdatePermission(expression = "user is a super service")
    @Column(name = "approval_team")
    private String approvalTeam;

    @Column(name = "tcl")
    private String tcl;

    @Column(name = "template_reference")
    private String templateReference;

    @ManyToOne
    private Organization organization;

    @ManyToOne
    private Workspace workspace;

    @UpdatePermission(expression = "user is a super service")
    @OneToMany(mappedBy = "job")
    private List<Step> step;

}

