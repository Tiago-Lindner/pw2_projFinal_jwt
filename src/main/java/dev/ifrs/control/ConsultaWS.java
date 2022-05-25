package dev.ifrs.control;

import java.util.List;

import javax.transaction.Transactional;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;

import dev.ifrs.model.Consulta;
import dev.ifrs.model.Paciente;
import dev.ifrs.model.Quiropraxista;

@Path("/consulta")
@Transactional
public class ConsultaWS {

   @GET
   @Path("/salvar/{texto}/{idCons}/{idPac}/{idQuiro}")
   @Produces(MediaType.APPLICATION_JSON)
   public Consulta salvar(@PathParam("texto") String texto, @PathParam("idCons") Long idCons, @PathParam("idPac") Long idPac, @PathParam("idQuiro") Long idQuiro){

    Consulta consulta = Consulta.findById(idCons);
    consulta.setSituacao(texto);
    consulta.persistAndFlush();

    Paciente pac = Paciente.findById(idPac);
    if (pac == null)
        throw new BadRequestException("Paciente não encontrado"); 

//ver post
    pac.addConsulta(consulta);
    consulta.setPaciente(pac);
    pac.persistAndFlush();

    Quiropraxista quiro = Quiropraxista.findById(idQuiro);
    if (quiro == null)
        throw new BadRequestException("Quiropraxista não encontrado"); 
    
    quiro.addConsulta(consulta);
    consulta.setQuiro(quiro);
    quiro.persistAndFlush();

    consulta.persist();

    return consulta;
   }

    @GET
    @Path("/listar")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public List<Consulta> list() {
        // 3 - O método `listAll` recupera todos os objetos da classe User.
        return Consulta.listAll();
        
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void addConsulta(@RequestBody IncluirConsulta consulta){
        Consulta cons = new Consulta();
        cons.setData(consulta.getData());
        cons.setHorario(consulta.getHorario());
        cons.persist();
    }
    
}
