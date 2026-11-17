package br.com.rotalivre.controller;

import br.com.rotalivre.dao.LocalAcessivelDao;
import br.com.rotalivre.dao.RelatoDao;
import br.com.rotalivre.dao.UsuarioDao;
import br.com.rotalivre.dto.LocalRequestDto;
import br.com.rotalivre.dto.LocalResponseDto;
import br.com.rotalivre.dto.RelatoRequestDto;
import br.com.rotalivre.dto.RelatoResponseDto;
import br.com.rotalivre.exception.EntidadeNaoEncontradaException;
import br.com.rotalivre.exception.RegraNegocioException;
import br.com.rotalivre.model.LocalAcessivel;
import br.com.rotalivre.model.Relato;
import br.com.rotalivre.model.Usuario;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

//Endpoints REST de local acessivel e dos relatos de cada local (1:N).
@RestController
@RequestMapping("locais")
public class LocalController {

    private final LocalAcessivelDao dao;
    private final RelatoDao relatoDao;
    private final UsuarioDao usuarioDao;
    private final ModelMapper mapper;

    public LocalController(LocalAcessivelDao dao, RelatoDao relatoDao, UsuarioDao usuarioDao, ModelMapper mapper) {
        this.dao = dao;
        this.relatoDao = relatoDao;
        this.usuarioDao = usuarioDao;
        this.mapper = mapper;
    }

    //POST /locais - 201 Created + Location (409 se o tipo nao existir)
    @PostMapping
    public ResponseEntity<LocalResponseDto> cadastrar(@Valid @RequestBody LocalRequestDto dto,
                                                      UriComponentsBuilder builder) throws SQLException {
        LocalAcessivel local = mapper.map(dto, LocalAcessivel.class);
        local.setAtivo(true);
        local.setDataCadastro(LocalDate.now());
        dao.cadastrar(local);

        URI uri = builder.path("/locais/{id}").buildAndExpand(local.getCodigo()).toUri();
        return ResponseEntity.created(uri).body(mapper.map(local, LocalResponseDto.class));
    }

    //GET /locais - 200 OK
    @GetMapping
    public List<LocalResponseDto> listar() throws SQLException {
        return dao.listar().stream().map(
                local -> mapper.map(local, LocalResponseDto.class)).toList();
    }

    //GET /locais/{id} - 200 OK ou 404
    @GetMapping("/{id}")
    public ResponseEntity<LocalResponseDto> buscar(@PathVariable int id)
            throws SQLException, EntidadeNaoEncontradaException {
        LocalAcessivel local = dao.pesquisarPorId(id);
        return ResponseEntity.ok(mapper.map(local, LocalResponseDto.class));
    }

    //PUT /locais/{id} - 200 OK, 400, 404 ou 409
    @PutMapping("/{id}")
    public ResponseEntity<Void> atualizar(@PathVariable int id, @Valid @RequestBody LocalRequestDto dto)
            throws SQLException, EntidadeNaoEncontradaException {
        LocalAcessivel local = mapper.map(dto, LocalAcessivel.class);
        local.setCodigo(id);
        dao.atualizar(local);
        return ResponseEntity.ok().build();
    }

    //DELETE /locais/{id} - 204 No Content, 404 ou 409 (se tiver relatos)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable int id)
            throws SQLException, EntidadeNaoEncontradaException {
        dao.remover(id);
        return ResponseEntity.noContent().build();
    }

    //GET /locais/{id}/relatos - 200 OK ou 404 (relatos do local, 1:N)
    @GetMapping("/{id}/relatos")
    public List<RelatoResponseDto> listarRelatos(@PathVariable int id)
            throws SQLException, EntidadeNaoEncontradaException {
        dao.pesquisarPorId(id); //404 se o local nao existir
        return relatoDao.listarPorLocal(id).stream().map(
                relato -> mapper.map(relato, RelatoResponseDto.class)).toList();
    }

    //POST /locais/{id}/relatos - 201 Created, 400, 404 ou 409 (regra: 1 relato por usuario, por local, por dia)
    @PostMapping("/{id}/relatos")
    public ResponseEntity<RelatoResponseDto> relatar(@PathVariable int id, @Valid @RequestBody RelatoRequestDto dto,
                                                     UriComponentsBuilder builder)
            throws SQLException, EntidadeNaoEncontradaException, RegraNegocioException {
        dao.pesquisarPorId(id); //404 se o local nao existir
        Usuario usuario = usuarioDao.pesquisarPorEmail(dto.getEmailUsuario()); //404 se o e-mail nao existir

        //Regra de negocio: o mesmo usuario nao relata o mesmo local duas vezes no mesmo dia
        if (relatoDao.existeRelatoHoje(usuario.getCodigo(), id))
            throw new RegraNegocioException("Voce ja relatou este local hoje. Tente novamente amanha");

        Relato relato = new Relato(0, id, usuario.getCodigo(), dto.getSituacao(), dto.getObservacao(), null);
        relatoDao.cadastrar(relato);
        relato = relatoDao.pesquisarPorId(relato.getCodigo()); //busca de novo para trazer a data gravada

        URI uri = builder.path("/relatos/{id}").buildAndExpand(relato.getCodigo()).toUri();
        return ResponseEntity.created(uri).body(mapper.map(relato, RelatoResponseDto.class));
    }
}
