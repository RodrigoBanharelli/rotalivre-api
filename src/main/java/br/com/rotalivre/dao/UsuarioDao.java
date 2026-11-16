package br.com.rotalivre.dao;

import br.com.rotalivre.exception.EntidadeNaoEncontradaException;
import br.com.rotalivre.model.Usuario;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//Persistencia de usuario no Oracle via JDBC (sem JPA).
@Repository
public class UsuarioDao {

    private static final String INSERT_SQL = "insert into tb_usuario " +
            " (cd_usuario, nm_usuario, ds_email, ds_perfil_mobilidade, dt_cadastro) " +
            " values (sq_tb_usuario.nextval, ?, ?, ?, ?)";
    private static final String SELECT_SQL = "select * from tb_usuario order by cd_usuario";
    private static final String SELECT_BY_ID_SQL = "select * from tb_usuario where cd_usuario = ?";
    private static final String UPDATE_SQL = "update tb_usuario set nm_usuario = ?, ds_email = ?, " +
            " ds_perfil_mobilidade = ? where cd_usuario = ?";
    private static final String DELETE_SQL = "delete from tb_usuario where cd_usuario = ?";
    //Conta outro usuario com o mesmo e-mail (ignora o proprio codigo no update).
    private static final String COUNT_EMAIL_SQL = "select count(*) from tb_usuario " +
            " where lower(ds_email) = lower(?) and cd_usuario <> ?";

    private final DataSource dataSource;

    //O Spring entrega o DataSource configurado no application.properties.
    public UsuarioDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void cadastrar(Usuario usuario) throws SQLException {
        try (Connection conexao = dataSource.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(INSERT_SQL, new String[]{"cd_usuario"})) {
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getPerfilMobilidade());
            stmt.setDate(4, Date.valueOf(usuario.getDataCadastro()));
            stmt.executeUpdate();

            //Pega o codigo gerado pela sequence
            ResultSet resultSet = stmt.getGeneratedKeys();
            if (resultSet.next())
                usuario.setCodigo(resultSet.getInt(1));
        }
    }

    public List<Usuario> listar() throws SQLException {
        try (Connection conexao = dataSource.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(SELECT_SQL)) {
            ResultSet resultSet = stmt.executeQuery();
            List<Usuario> lista = new ArrayList<>();
            while (resultSet.next())
                lista.add(getUsuario(resultSet));
            return lista;
        }
    }

    public Usuario pesquisarPorId(int id) throws SQLException, EntidadeNaoEncontradaException {
        try (Connection conexao = dataSource.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(SELECT_BY_ID_SQL)) {
            stmt.setInt(1, id);
            ResultSet resultSet = stmt.executeQuery();
            if (!resultSet.next())
                throw new EntidadeNaoEncontradaException("Usuario nao encontrado");
            return getUsuario(resultSet);
        }
    }

    public void atualizar(Usuario usuario) throws SQLException, EntidadeNaoEncontradaException {
        try (Connection conexao = dataSource.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(UPDATE_SQL)) {
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getPerfilMobilidade());
            stmt.setInt(4, usuario.getCodigo());
            int linhas = stmt.executeUpdate();
            if (linhas == 0)
                throw new EntidadeNaoEncontradaException("Usuario nao encontrado para atualizar");
        }
    }

    //Se o usuario tiver relatos, a FK do banco impede e o handler devolve 409.
    public void remover(int id) throws SQLException, EntidadeNaoEncontradaException {
        try (Connection conexao = dataSource.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(DELETE_SQL)) {
            stmt.setInt(1, id);
            int linhas = stmt.executeUpdate();
            if (linhas == 0)
                throw new EntidadeNaoEncontradaException("Usuario nao encontrado para remover");
        }
    }

    //Regra: e-mail unico. Retorna true se outro usuario ja usa este e-mail.
    public boolean emailEmUso(String email, int codigoIgnorado) throws SQLException {
        try (Connection conexao = dataSource.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(COUNT_EMAIL_SQL)) {
            stmt.setString(1, email);
            stmt.setInt(2, codigoIgnorado);
            ResultSet resultSet = stmt.executeQuery();
            resultSet.next();
            return resultSet.getInt(1) > 0;
        }
    }

    //Converte a linha do banco (cd_, nm_, ds_, dt_) no objeto Usuario.
    private Usuario getUsuario(ResultSet resultSet) throws SQLException {
        int codigo = resultSet.getInt("cd_usuario");
        String nome = resultSet.getString("nm_usuario");
        String email = resultSet.getString("ds_email");
        String perfil = resultSet.getString("ds_perfil_mobilidade");
        Date data = resultSet.getDate("dt_cadastro");
        return new Usuario(codigo, nome, email, perfil, data.toLocalDate());
    }
}
