package br.com.rotalivre.dao;

import br.com.rotalivre.exception.EntidadeNaoEncontradaException;
import br.com.rotalivre.model.LocalAcessivel;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//Persistencia de local no Oracle via JDBC (sem JPA).
@Repository
public class LocalAcessivelDao {

    private static final String INSERT_SQL = "insert into tb_local_acessivel " +
            " (cd_local, cd_tipo_local, nm_local, ds_endereco, vl_latitude, vl_longitude, " +
            " vl_inclinacao_pct, vl_largura_cm, st_ativo, dt_cadastro) " +
            " values (sq_tb_local_acessivel.nextval, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SELECT_SQL = "select * from tb_local_acessivel order by cd_local";
    private static final String SELECT_BY_ID_SQL = "select * from tb_local_acessivel where cd_local = ?";
    private static final String UPDATE_SQL = "update tb_local_acessivel set cd_tipo_local = ?, nm_local = ?, " +
            " ds_endereco = ?, vl_latitude = ?, vl_longitude = ?, vl_inclinacao_pct = ?, vl_largura_cm = ? " +
            " where cd_local = ?";
    private static final String DELETE_SQL = "delete from tb_local_acessivel where cd_local = ?";

    private final DataSource dataSource;

    public LocalAcessivelDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    //Se o codigo do tipo nao existir, a FK do banco impede e o handler devolve 409.
    public void cadastrar(LocalAcessivel local) throws SQLException {
        try (Connection conexao = dataSource.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(INSERT_SQL, new String[]{"cd_local"})) {
            stmt.setInt(1, local.getCodigoTipoLocal());
            stmt.setString(2, local.getNome());
            stmt.setString(3, local.getEndereco());
            stmt.setDouble(4, local.getLatitude());
            stmt.setDouble(5, local.getLongitude());
            stmt.setDouble(6, local.getInclinacaoPct());
            stmt.setInt(7, local.getLarguraCm());
            stmt.setString(8, local.isAtivo() ? "S" : "N"); //boolean do Java vira S/N no banco
            stmt.setDate(9, Date.valueOf(local.getDataCadastro()));
            stmt.executeUpdate();

            ResultSet resultSet = stmt.getGeneratedKeys();
            if (resultSet.next())
                local.setCodigo(resultSet.getInt(1));
        }
    }

    public List<LocalAcessivel> listar() throws SQLException {
        try (Connection conexao = dataSource.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(SELECT_SQL)) {
            ResultSet resultSet = stmt.executeQuery();
            List<LocalAcessivel> lista = new ArrayList<>();
            while (resultSet.next())
                lista.add(getLocal(resultSet));
            return lista;
        }
    }

    public LocalAcessivel pesquisarPorId(int id) throws SQLException, EntidadeNaoEncontradaException {
        try (Connection conexao = dataSource.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(SELECT_BY_ID_SQL)) {
            stmt.setInt(1, id);
            ResultSet resultSet = stmt.executeQuery();
            if (!resultSet.next())
                throw new EntidadeNaoEncontradaException("Local nao encontrado");
            return getLocal(resultSet);
        }
    }

    public void atualizar(LocalAcessivel local) throws SQLException, EntidadeNaoEncontradaException {
        try (Connection conexao = dataSource.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(UPDATE_SQL)) {
            stmt.setInt(1, local.getCodigoTipoLocal());
            stmt.setString(2, local.getNome());
            stmt.setString(3, local.getEndereco());
            stmt.setDouble(4, local.getLatitude());
            stmt.setDouble(5, local.getLongitude());
            stmt.setDouble(6, local.getInclinacaoPct());
            stmt.setInt(7, local.getLarguraCm());
            stmt.setInt(8, local.getCodigo());
            int linhas = stmt.executeUpdate();
            if (linhas == 0)
                throw new EntidadeNaoEncontradaException("Local nao encontrado para atualizar");
        }
    }

    //Se o local tiver relatos ou recursos, a FK do banco impede e o handler devolve 409.
    public void remover(int id) throws SQLException, EntidadeNaoEncontradaException {
        try (Connection conexao = dataSource.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(DELETE_SQL)) {
            stmt.setInt(1, id);
            int linhas = stmt.executeUpdate();
            if (linhas == 0)
                throw new EntidadeNaoEncontradaException("Local nao encontrado para remover");
        }
    }

    //Converte a linha do banco no objeto LocalAcessivel.
    private LocalAcessivel getLocal(ResultSet resultSet) throws SQLException {
        int codigo = resultSet.getInt("cd_local");
        int codigoTipo = resultSet.getInt("cd_tipo_local");
        String nome = resultSet.getString("nm_local");
        String endereco = resultSet.getString("ds_endereco");
        double latitude = resultSet.getDouble("vl_latitude");
        double longitude = resultSet.getDouble("vl_longitude");
        double inclinacao = resultSet.getDouble("vl_inclinacao_pct");
        int largura = resultSet.getInt("vl_largura_cm");
        boolean ativo = resultSet.getString("st_ativo").equals("S");
        Date data = resultSet.getDate("dt_cadastro");
        return new LocalAcessivel(codigo, codigoTipo, nome, endereco, latitude, longitude,
                inclinacao, largura, ativo, data.toLocalDate());
    }
}
