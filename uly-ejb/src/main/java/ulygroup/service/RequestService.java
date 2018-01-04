/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ulygroup.service;

import org.jboss.logging.Logger;
import ulygroup.model.Request;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;

// The @Stateless annotation eliminates the need for manual transaction demarcation
@Stateless
public class RequestService {

    private static final Logger LOGGER = Logger.getLogger(RequestService.class); 

    @Inject
    private EntityManager em;

    @Inject
    private Event<Request> requestEventSrc;

    public void submit(Request request) {
        LOGGER.debug("Submitting " + request);
        if (request.getId() == 0) {
            em.persist(request);
            requestEventSrc.fire(request);  // caught in RequestListProducer
        }
        else {
            Request tmpReq = findById(request.getId());
            tmpReq.setSum(request.getSum());
            em.persist(tmpReq);
            requestEventSrc.fire(tmpReq);
        }
    }
    
    public void remove(long id) {
        LOGGER.info("Removing request id=" + id);
        Request r = em.find(Request.class, id);
        em.remove(r);
        requestEventSrc.fire(r);
    }
    
    public Request findById(Long id) {
        LOGGER.debug("findById() id=" + id);
        return em.find(Request.class, id);
    }
}
