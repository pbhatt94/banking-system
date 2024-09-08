package com.wg.banking.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.wg.banking.constants.AccountConstants;
import com.wg.banking.model.AccountDetails;

public class AccountDetailsDAO extends GenericDAO<AccountDetails> {

	@Override
	protected AccountDetails mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        String accountNo = resultSet.getString(AccountConstants.ACCOUNT_NO_COLUMN);
        String ownerName = resultSet.getString("name");
        double balance = resultSet.getDouble(AccountConstants.BALANCE_COLUMN);
        String branch = resultSet.getString("branch");

        // Create and return AccountDetails object
        return new AccountDetails(accountNo, ownerName, balance, branch);
	}
	
	
	public List<AccountDetails> getAllAccountDetails() throws ClassNotFoundException, SQLException {
		String query = "select account_no, u.name, balance, b.name as branch from Account a JOIN User u ON a.owner_id = u.id JOIN Branch b on a.branch_id = b.id";
		return getAll(query);
	}
}
