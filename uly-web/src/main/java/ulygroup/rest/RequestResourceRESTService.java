package ulygroup.rest;

import org.jboss.logging.Logger;
import ulygroup.data.RequestRepository;
import ulygroup.model.Request;
import ulygroup.service.RequestService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A JAX-RS RESTful service to read/write the contents of the Requests table.
 */
@Path("/requests")
@RequestScoped
public class RequestResourceRESTService {

    private static final Logger LOGGER = Logger.getLogger(RequestResourceRESTService.class);
    
    @Inject
    private Validator validator;

    @Inject
    private RequestRepository requestRepository;

    @Inject
    RequestService requestService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Request> listAllRequests() {
        return requestRepository.findAll(RequestRepository.Filter.All, null, "listAll");
    }

    @GET
    @Path("/{id:[0-9][0-9]*}")
    @Produces(MediaType.APPLICATION_JSON)
    public Request lookupRequestById(@PathParam("id") long id) {
        Request request = requestRepository.findById(id);
        if (request == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return request;
    }

    /**
     * Creates a new request from the values provided, with validation.
     * @return a JAX-RS response with either 200 ok, or with a map of fields, and related errors.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createRequest(Request request) {

        Response.ResponseBuilder builder;

        try {
            validateRequest(request);

            requestService.persist(request);
            
            builder = Response.ok();
        }
        catch (ConstraintViolationException ce) {
            builder = createViolationResponse(ce.getConstraintViolations());
        }
        catch (ValidationException e) {
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("email", "Email taken");
            builder = Response.status(Response.Status.CONFLICT).entity(responseObj);
        }
        catch (Exception e) {
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("error", e.getMessage());
            builder = Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
        }

        return builder.build();
    }

    private void validateRequest(Request request) throws ValidationException {

        Set<ConstraintViolation<Request>> violations = validator.validate(request);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
        }
    }

    // Creates a JAX-RS "Bad Request" response including a map of all violation fields, and their message.
    private Response.ResponseBuilder createViolationResponse(Set<ConstraintViolation<?>> violations) {
        LOGGER.debug("Validation completed. violations found: " + violations.size());

        Map<String, String> responseObj = new HashMap<>();

        for (ConstraintViolation<?> violation : violations) {
            responseObj.put(violation.getPropertyPath().toString(), violation.getMessage());
        }

        return Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
    }
}
