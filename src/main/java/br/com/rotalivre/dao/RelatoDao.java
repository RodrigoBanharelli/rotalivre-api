package br.com.rotalivre.dao;

import br.com.rotalivre.exception.EntidadeNaoEncontradaException;
import br.com.rotalivre.model.Relato;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

//Persistencia de relato no Oracle via JDBC (sem JPA).
@Repository
public class RelatoDao {

    //A data e o sysdate do Oracle: o "mesmo dia" da regra usa sempre o relogio do banco.
    private static final String INSERT_SQL = "insert into tb_relato " +
            " (cd_relato, cd_local, cd_usuario, ds_situacao, ds_observacao, dt_relato) " +
            " values (sq_tb_relato.nextval, ?, ?, ?, ?, sysdate)";
    private static final String SELECT_SQL = "select * from tb_relato order by dt_relato desc";
    private static final String SELECT_BY_ID_SQL = "select * from tb_relato where cd_relato = ?";
    private static final String SELECT_BY_LOCAL_SQL = "select * from tb_relato where cd_local = ? " +
            " order by dt_relato desc";
    private static final String SELECT_BY_USUARIO_SQL = "select * from tb_relato where cd_usuario = ? " +
            " order by dt_relato desc";
    private static final String UPDATE_SQL = "update tb_relato set ds_situacao = ?, ds_observacao = ? " +
            " where cd_relato = ?";
    private static final String DELETE_SQL = "delete from tb_relato where cd_relato = ?";
    //Regra: conta relatos do usuario neste local feitos hoje
    private static final String COUNT_HOJE_SQL = "select count(*) from tb_relato " +
            " where cd_usuario = ? and cd_local = ? and trunc(dt_relato) = trunc(sysdate)";

    private final DataSource dataSource;

    public RelatoDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void cadastrar(Relato relato) throws SQLException {
        try (Connection conexao = dataSource.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(INSERT_SQL, new String[]{"cd_relato"})) {
            stmt.setInt(1, relato.getCodigoLocal());
            stmt.setInt(2, relato.getCodigoUsuario());
            stmt.setString(3, relato.getSituacao());
            stmt.setString(4, relato.getObservacao());
            stmt.executeUpdate();

            ResultSet resultSet = stmt.getGeneratedKeys();
            if (resultSet.next())
                relato.setCodigo(resultSet.getInt(1));
        }
    }

    public List<Relato> listar() throws SQLException {
        try (Connection conexao = dataSource.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(SELECT_SQL)) {
            return getLista(stmt);
        }
    }

    public List<Relato> listarPorLocal(int codigoLocal) throws SQLException {
        try (Connection conexao = dataSource.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(SELECT_BY_LOCAL_SQL)) {
            stmt.setInt(1, codigoLocal);
            return getLista(stmt);
        }
    }

    public List<Relato> listarPorUsuario(int codigoUsuario) throws SQLException {
        try (Connection conexao = dataSource.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(SELECT_BY_USUARIO_SQL)) {
            stmt.setInt(1, codigoUsuario);
            return getLista(stmt);
        }
    }

    public Relato pesquisarPorId(int id) throws SQLException, EntidadeNaoEncontradaException {
        try (Connection conexao = dataSource.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(SELECT_BY_ID_SQL)) {
            stmt.setInt(1, id);
            ResultSet resultSet = stmt.executeQuery();
            if (!resultSet.next())
                throw new EntidadeNaoEncontradaException("Relato nao encontrado");
            return getRelato(resultSet);
        }
    }

    public void atualizar(Relato relato) throws SQLException, EntidadeNaoEncontradaException {
        try (Connection conexao = dataSource.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(UPDATE_SQL)) {
            stmt.setString(1, relato.getSituacao());
            stmt.setString(2, relato.getObservacao());
            stmt.setInt(3, relato.getCodigo());
            int linhas = stmt.executeUpdate();
            if (linhas == 0)
                throw new EntidadeNaoEncontradaException("Relato nao encontrado para atualizar");
        }
    }

    public void remover(int id) throws SQLException, EntidadeNaoEncontradaException {
        try (Connection conexao = dataSource.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(DELETE_SQL)) {
            stmt.setInt(1, id);
            int linhas = stmt.executeUpdate();
            if (linhas == 0)
                throw new EntidadeNaoEncontradaException("Relato nao encontrado para remover");
        }
    }

    //Regra de negocio: true se o usuario ja relatou este local hoje.
    public boolean existeRelatoHoje(int codigoUsuario, int codigoLocal) throws SQLException {
        try (Connection conexao = dataSource.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(COUNT_HOJE_SQL)) {
            stmt.setInt(1, codigoUsuario);
            stmt.setInt(2, codigoLocal);
            ResultSet resultSet = stmt.executeQuery();
            resultSet.next();
            return resultSet.getInt(1) > 0;
        }
    }

    private List<Relato> getLista(PreparedStatement stmt) throws SQLException {
        ResultSet resultSet = stmt.executeQuery();
        List<Relato> lista = new ArrayList<>();
        while (resultSet.next())
            lista.add(getRelato(resultSet));
        return lista;
    }

    //Converte a linha do banco no objeto Relato.
    private Relato getRelato(ResultSet resultSet) throws SQLException {
        int codigo = resultSet.getInt("cd_relato");
        int codigoLocal = resultSet.getInt("cd_local");
        int codigoUsuario = resultSet.getInt("cd_usuario");
        String situacao = resultSet.getString("ds_situacao");
        String observacao = resultSet.getString("ds_observacao");
        Timestamp data = resultSet.getTimestamp("dt_relato");
        return new Relato(codigo, codigoLocal, codigoUsuario, situacao, observacao, data.toLocalDateTime());
    }
}
