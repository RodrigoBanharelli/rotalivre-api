package br.com.rotalivre.controller;

import br.com.rotalivre.dao.RelatoDao;
import br.com.rotalivre.dao.UsuarioDao;
import br.com.rotalivre.dto.RelatoResponseDto;
import br.com.rotalivre.dto.UsuarioRequestDto;
import br.com.rotalivre.dto.UsuarioResponseDto;
import br.com.rotalivre.exception.EntidadeNaoEncontradaException;
import br.com.rotalivre.exception.RegraNegocioException;
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

//Endpoints REST de usuario e dos relatos de cada usuario (1:N).
@RestController
@RequestMapping("usuarios")
public class UsuarioController {

    private final UsuarioDao dao;
    private final RelatoDao relatoDao;
    private final ModelMapper mapper;

    public UsuarioController(UsuarioDao dao, RelatoDao relatoDao, ModelMapper mapper) {
        this.dao = dao;
        this.relatoDao = relatoDao;
        this.mapper = mapper;
    }

    //GET /usuarios/{id}/relatos - 200 OK ou 404 (relatos do usuario, 1:N)
    @GetMapping("/{id}/relatos")
    public List<RelatoResponseDto> listarRelatos(@PathVariable int id)
            throws SQLException, EntidadeNaoEncontradaException {
        dao.pesquisarPorId(id); //404 se o usuario nao existir
        return relatoDao.listarPorUsuario(id).stream().map(
                relato -> mapper.map(relato, RelatoResponseDto.class)).toList();
    }

    //POST /usuarios - 201 Created + Location
    @PostMapping
    public ResponseEntity<UsuarioResponseDto> cadastrar(@Valid @RequestBody UsuarioRequestDto dto,
                                                        UriComponentsBuilder builder)
            throws SQLException, RegraNegocioException {
        //Regra: e-mail unico (409)
        if (dao.emailEmUso(dto.getEmail(), 0))
            throw new RegraNegocioException("Ja existe um usuario com este e-mail");

        Usuario usuario = mapper.map(dto, Usuario.class);
        usuario.setDataCadastro(LocalDate.now());
        dao.cadastrar(usuario);

        URI uri = builder.path("/usuarios/{id}").buildAndExpand(usuario.getCodigo()).toUri();
        return ResponseEntity.created(uri).body(mapper.map(usuario, UsuarioResponseDto.class));
    }

    //GET /usuarios - 200 OK
    @GetMapping
    public List<UsuarioResponseDto> listar() throws SQLException {
        return dao.listar().stream().map(
                usuario -> mapper.map(usuario, UsuarioResponseDto.class)).toList();
    }

    //GET /usuarios/{id} - 200 OK ou 404
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDto> buscar(@PathVariable int id)
            throws SQLException, EntidadeNaoEncontradaException {
        Usuario usuario = dao.pesquisarPorId(id);
        return ResponseEntity.ok(mapper.map(usuario, UsuarioResponseDto.class));
    }

    //PUT /usuarios/{id} - 200 OK, 404 ou 409
    @PutMapping("/{id}")
    public ResponseEntity<Void> atualizar(@PathVariable int id, @Valid @RequestBody UsuarioRequestDto dto)
            throws SQLException, EntidadeNaoEncontradaException, RegraNegocioException {
        //Regra: o e-mail so pode repetir se for do proprio usuario (409)
        if (dao.emailEmUso(dto.getEmail(), id))
            throw new RegraNegocioException("Ja existe outro usuario com este e-mail");

        Usuario usuario = mapper.map(dto, Usuario.class);
        usuario.setCodigo(id);
        dao.atualizar(usuario);
        return ResponseEntity.ok().build();
    }

    //DELETE /usuarios/{id} - 204 No Content, 404 ou 409 (se tiver relatos)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable int id)
            throws SQLException, EntidadeNaoEncontradaException {
        dao.remover(id);
        return ResponseEntity.noContent().build();
    }
}
