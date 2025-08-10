package com.phumlanidev.productservice.utils;


import com.phumlanidev.productservice.model.AuditLog;
import org.springframework.data.jpa.domain.Specification;

public class AuditLogSpecifications {

  public static Specification<AuditLog> hasUserId(String userId) {
    return (root, query, cb) -> userId == null ? null :
            cb.equal(root.get("userId"), userId);
  }

  public static Specification<AuditLog> hasAction(String action) {
    return (root, query, cb) -> action == null ? null :
            cb.equal(root.get("action"), action);
  }
}
