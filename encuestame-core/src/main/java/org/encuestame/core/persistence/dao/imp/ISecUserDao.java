/*
 ************************************************************************************
 * Copyright (C) 2001-2009 encuestame: system online surveys Copyright (C) 2009
 * encuestame Development Team.
 * Licensed under the Apache Software License version 2.0
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to  in writing,  software  distributed
 * under the License is distributed  on  an  "AS IS"  BASIS,  WITHOUT  WARRANTIES  OR
 * CONDITIONS OF ANY KIND, either  express  or  implied.  See  the  License  for  the
 * specific language governing permissions and limitations under the License.
 ************************************************************************************
 */
package org.encuestame.core.persistence.dao.imp;

import java.util.List;

import org.encuestame.core.persistence.pojo.SecUserSecondary;
import org.encuestame.core.persistence.pojo.SecUsers;
import org.hibernate.HibernateException;

/**
 * Interface SecUsers.
 *
 * @author Picado, Juan juan@encuestame.org
 * @since May 05, 2009
 * @version $Id$
 */
public interface ISecUserDao extends IBaseDao {

    /**
     * @param username username
     * @return {@link SecUserSecondary}
     * @throws HibernateException HibernateException
     */
    SecUserSecondary getUserByUsername(final String username);


    /**
     * @return List {@link SecUserSecondary}
     * @throws HibernateException HibernateException
     */
     List<SecUserSecondary> findAll();

     /**
      * Retrieve List of Secondary users without owner account.
      * @param secUsers {@link SecUsers}.
      * @return List of {@link SecUserSecondary}
      */
     List<SecUserSecondary> retrieveListOwnerUsers(final SecUsers	secUsers);


    /**
     * @param userId userId
     * @return {@link SecUsers}
     * @throws HibernateException HibernateException
     */
    SecUsers getUserById(final Long userId);

    /**
     * @param userId userId
     * @return {@link SecUserSecondary}
     * @throws HibernateException HibernateException
     */
    SecUserSecondary getSecondaryUserById(final Long userId);

    /**
     * Get list of users by username.
     * @param username username
     * @return list of users
     */
    List<SecUserSecondary> getUsersByUsername(final String username);

    /**
     * Get {@link SecUserSecondary} but {@link SecUsers} id.
     * @param userId user id
     * @return secondary user list
     */
    List<SecUserSecondary> getSecondaryUsersByUserId(final Long userId);

    /**
     * Search user by email
     * @param email email
     * @return
     */
    List<SecUserSecondary> searchUsersByEmail(final String email);

}