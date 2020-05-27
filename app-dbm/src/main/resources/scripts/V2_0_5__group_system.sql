ALTER TABLE dte_grp DISABLE TRIGGER dte_grp_check;
update dte_grp SET grp_label = 'System', grp_icon = 'dte_logo' WHERE grp_id = 0;
ALTER TABLE dte_grp ENABLE TRIGGER dte_grp_check;
