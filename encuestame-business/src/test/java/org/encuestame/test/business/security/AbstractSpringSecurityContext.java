/*
 ************************************************************************************
 * Copyright (C) 2001-2010 encuestame: system online surveys Copyright (C) 2009
 * encuestame Development Team.
 * Licensed under the Apache Software License version 2.0
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to  in writing,  software  distributed
 * under the License is distributed  on  an  "AS IS"  BASIS,  WITHOUT  WARRANTIES  OR
 * CONDITIONS OF ANY KIND, either  express  or  implied.  See  the  License  for  the
 * specific language governing permissions and limitations under the License.
 ************************************************************************************
 */
package org.encuestame.test.business.security;

import java.util.ArrayList;
import java.util.List;

import org.encuestame.core.persistence.domain.security.SecUserSecondary;
import org.encuestame.core.security.EnMePermission;
import org.encuestame.test.config.AbstractBaseUnitBeans;
import org.junit.Before;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Abstract Security Base Test.
 * @author Picado, Juan juanATencuestame.org
 * @since Jun 30, 2010 9:03:50 PM
 * @version $Id:$
 */
public abstract class AbstractSpringSecurityContext extends AbstractBaseUnitBeans {

    /**
     * {@link SecUserSecondary}.
     */
    public SecUserSecondary secondary;

    /**
     * Username of User Logged.
     */
    private String usernameLogged;

    @Before
    public void setUp() throws Exception {
       this.secondary = createSecondaryUser("admin", createUser());
       createPermission("ENCUESTAME_USER");
       createPermission("ENCUESTAME_ADMIN");
       //TODO:  maybe we need more specific here.
       setAuthentication(this.secondary.getUsername(), "12345");
    }


    /**
     * @return the secondary
     */
    public SecUserSecondary getSecondary() {
        return secondary;
    }

    /**
     * @param secondary the secondary to set
     */
    public void setSecondary(SecUserSecondary secondary) {
        this.secondary = secondary;
    }

    /**
     * set Authentication.
     * @param username
     * @param password
     */
    public void setAuthentication(final String username, final String password){
        final List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        //Add permissions.
        authorities.add(new GrantedAuthorityImpl(EnMePermission.ENCUESTAME_USER.name()));
        authorities.add(new GrantedAuthorityImpl(EnMePermission.ENCUESTAME_ADMIN.name()));
        TestingAuthenticationToken token = new TestingAuthenticationToken(username, password, authorities);
        token.setAuthenticated(true);
        SecurityContextHolder.getContext().setAuthentication(token);
        //System.out.println("creando SecurityContextHolder "+SecurityContextHolder.getContext().getAuthentication());
        setUsernameLogged(this.secondary.getUsername());
    }

    /**
     * Get {@link Authentication}.
     * @return
     */
    public Authentication getAuthentication() {
        log.debug("Security "+ SecurityContextHolder.getContext().getAuthentication());
        return  SecurityContextHolder.getContext().getAuthentication();
    }


    /**
     * @return the usernameLogged
     */
    public String getUsernameLogged() {
        return usernameLogged;
    }


    /**
     * @param usernameLogged the usernameLogged to set
     */
    public void setUsernameLogged(String usernameLogged) {
        this.usernameLogged = usernameLogged;
    }
}