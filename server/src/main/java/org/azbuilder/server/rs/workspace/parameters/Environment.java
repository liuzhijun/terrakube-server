package org.azbuilder.server.rs.workspace.parameters;

import com.yahoo.elide.annotation.Include;
import lombok.Getter;
import lombok.Setter;
import org.azbuilder.server.rs.workspace.Workspace;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.UUID;

@Include(type = "environment")
@Getter
@Setter
@Entity
public class Environment {

    @Id
    @GeneratedValue
    private UUID id;

    private String key;
    private String value;

    @ManyToOne
    private Workspace workspace;
}
