package br.com.rotalivre.controller;

import br.com.rotalivre.dao.RelatoDao;
import br.com.rotalivre.dao.UsuarioDao;
import br.com.rotalivre.dto.RelatoRequestDto;
import br.com.rotalivre.dto.RelatoResponseDto;
import br.com.rotalivre.exception.EntidadeNaoEncontradaException;
import br.com.rotalivre.exception.RegraNegocioException;
import br.com.rotalivre.model.Relato;
import br.com.rotalivre.model.Usuario;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

//Endpoints REST de relato. O cadastro fica na rota aninhada POST /locais/{id}/relatos.
@RestController
@RequestMapping("relatos")
public class RelatoController {

    private final RelatoDao dao;
    private final UsuarioDao usuarioDao;
    private final ModelMapper mapper;

    public RelatoController(RelatoDao dao, UsuarioDao usuarioDao, ModelMapper mapper) {
        this.dao = dao;
        this.usuarioDao = usuarioDao;
        this.mapper = mapper;
    }

    //GET /relatos - 200 OK
    @GetMapping
    public List<RelatoResponseDto> listar() throws SQLException {
        return dao.listar().stream().map(
                relato -> mapper.map(relato, RelatoResponseDto.class)).toList();
    }

    //GET /relatos/{id} - 200 OK ou 404
    @GetMapping("/{id}")
    public ResponseEntity<RelatoResponseDto> buscar(@PathVariable int id)
            throws SQLException, EntidadeNaoEncontradaException {
        Relato relato = dao.pesquisarPorId(id);
        return ResponseEntity.ok(mapper.map(relato, RelatoResponseDto.class));
    }

    //PUT /relatos/{id} - 200 OK, 400, 404 ou 409 (so quem fez o relato pode alterar)
    @PutMapping("/{id}")
    public ResponseEntity<Void> atualizar(@PathVariable int id, @Valid @RequestBody RelatoRequestDto dto)
            throws SQLException, EntidadeNaoEncontradaException, RegraNegocioException {
        Relato relato = dao.pesquisarPorId(id);
        Usuario usuario = usuarioDao.pesquisarPorEmail(dto.getEmailUsuario());
        if (relato.getCodigoUsuario() != usuario.getCodigo())
            throw new RegraNegocioException("Somente quem fez o relato pode altera-lo");

        relato.setSituacao(dto.getSituacao());
        relato.setObservacao(dto.getObservacao());
        dao.atualizar(relato);
        return ResponseEntity.ok().build();
    }

    //DELETE /relatos/{id} - 204 No Content ou 404
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable int id)
            throws SQLException, EntidadeNaoEncontradaException {
        dao.remover(id);
        return ResponseEntity.noContent().build();
    }
}
