package org.azbuilder.api.repository;

import org.azbuilder.api.rs.workspace.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface WorkspaceRepository extends JpaRepository<Workspace, UUID> {
}
