//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.04.25 at 07:11:10 PM PKT 
//


package com.softech.vu360.lms.webservice.message.lmsapi.types.usergroup;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.softech.vu360.lms.webservice.message.lmsapi.types.usergroup package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.softech.vu360.lms.webservice.message.lmsapi.types.usergroup
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link UserGroup }
     * 
     */
    public UserGroup createUserGroup() {
        return new UserGroup();
    }

    /**
     * Create an instance of {@link NewUserGroups }
     * 
     */
    public NewUserGroups createNewUserGroups() {
        return new NewUserGroups();
    }

    /**
     * Create an instance of {@link UpdatedUserGroup }
     * 
     */
    public UpdatedUserGroup createUpdatedUserGroup() {
        return new UpdatedUserGroup();
    }

    /**
     * Create an instance of {@link ResponseUserGroup }
     * 
     */
    public ResponseUserGroup createResponseUserGroup() {
        return new ResponseUserGroup();
    }

    /**
     * Create an instance of {@link DeleteUserGroups }
     * 
     */
    public DeleteUserGroups createDeleteUserGroups() {
        return new DeleteUserGroups();
    }

    /**
     * Create an instance of {@link DeletedUserGroup }
     * 
     */
    public DeletedUserGroup createDeletedUserGroup() {
        return new DeletedUserGroup();
    }

    /**
     * Create an instance of {@link InvalidUserGroups }
     * 
     */
    public InvalidUserGroups createInvalidUserGroups() {
        return new InvalidUserGroups();
    }

    /**
     * Create an instance of {@link GetUserGroupById }
     * 
     */
    public GetUserGroupById createGetUserGroupById() {
        return new GetUserGroupById();
    }

    /**
     * Create an instance of {@link GetUserGroupIdByName }
     * 
     */
    public GetUserGroupIdByName createGetUserGroupIdByName() {
        return new GetUserGroupIdByName();
    }

    /**
     * Create an instance of {@link InvalidUserGroupValueWithError }
     * 
     */
    public InvalidUserGroupValueWithError createInvalidUserGroupValueWithError() {
        return new InvalidUserGroupValueWithError();
    }

}
