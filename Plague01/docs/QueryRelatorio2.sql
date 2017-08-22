SELECT
     boletimdiario.`dataHoraInicio` AS boletimdiario_dataHoraInicio,
     boletimdiario.`horimetro` AS boletimdiario_horimetro,
     boletimdiario.`idFirebase` AS boletimdiario_idFirebase,
     boletimdiario.`maquinaBase` AS boletimdiario_maquinaBase,
     boletimdiario.`matriculaResponsavel` AS boletimdiario_matriculaResponsavel,
     boletimdiario.`nomeResponsavel` AS boletimdiario_nomeResponsavel,
     boletimdiario.`odometro` AS boletimdiario_odometro,
     boletimdiario.`projeto` AS boletimdiario_projeto,
     boletimdiario.`turno` AS boletimdiario_turno,
     boletimdiario.`fazenda_id` AS boletimdiario_fazenda_id,
     boletimdiario.`CHECK_LIST_ID` AS boletimdiario_CHECK_LIST_ID,
     boletimdiario.`id` AS boletimdiario_id,
     boletimchecklist.`id` AS boletimchecklist_id,
     boletimchecklist.`adesivosSeguranca` AS boletimchecklist_adesivosSeguranca,
     boletimchecklist.`alarmesPainelInstrumentos` AS boletimchecklist_alarmesPainelInstrumentos,
     boletimchecklist.`assentoOperacao` AS boletimchecklist_assentoOperacao,
     boletimchecklist.`chaveGeral` AS boletimchecklist_chaveGeral,
     boletimchecklist.`condicoesPneus` AS boletimchecklist_condicoesPneus,
     boletimchecklist.`conjuntoCorteHARVESTER` AS boletimchecklist_conjuntoCorteHARVESTER,
     boletimchecklist.`discoEDentesCorteFELLER` AS boletimchecklist_discoEDentesCorteFELLER,
     boletimchecklist.`escadasOuEstribosAcesso` AS boletimchecklist_escadasOuEstribosAcesso,
     boletimchecklist.`faroisTrabalho` AS boletimchecklist_faroisTrabalho,
     boletimchecklist.`freioEmergencia` AS boletimchecklist_freioEmergencia,
     boletimchecklist.`freioServicos` AS boletimchecklist_freioServicos,
     boletimchecklist.`idFirebase` AS boletimchecklist_idFirebase,
     boletimchecklist.`inspecaoExtintores` AS boletimchecklist_inspecaoExtintores,
     boletimchecklist.`janelasLaterais` AS boletimchecklist_janelasLaterais,
     boletimchecklist.`kitContencaoVazamentos` AS boletimchecklist_kitContencaoVazamentos,
     boletimchecklist.`limpezaEsteiras` AS boletimchecklist_limpezaEsteiras,
     boletimchecklist.`limpezaInternaCabine` AS boletimchecklist_limpezaInternaCabine,
     boletimchecklist.`limpezaRadiador` AS boletimchecklist_limpezaRadiador,
     boletimchecklist.`lubrificacaoGeral` AS boletimchecklist_lubrificacaoGeral,
     boletimchecklist.`nivelLiquidoRefrigerante` AS boletimchecklist_nivelLiquidoRefrigerante,
     boletimchecklist.`nivelOleoHidraulico` AS boletimchecklist_nivelOleoHidraulico,
     boletimchecklist.`nivelOleoMotor` AS boletimchecklist_nivelOleoMotor,
     boletimchecklist.`vazamentoMangueirasECilindros` AS boletimchecklist_vazamentoMangueirasECilindros,
     boletimchecklist.`travaCabineFORWARDER` AS boletimchecklist_travaCabineFORWARDER,
     boletimchecklist.`tampasProtecao` AS boletimchecklist_tampasProtecao,
     boletimchecklist.`tensaoEsteiras` AS boletimchecklist_tensaoEsteiras,
     boletimchecklist.`sinalizacaoReEMovimento` AS boletimchecklist_sinalizacaoReEMovimento,
     boletimchecklist.`sapatasEsteira` AS boletimchecklist_sapatasEsteira,
     boletimchecklist.`rotator` AS boletimchecklist_rotator,
     boletimchecklist.`roletesInferiores` AS boletimchecklist_roletesInferiores,
     boletimchecklist.`roletesSuperiores` AS boletimchecklist_roletesSuperiores,
     boletimchecklist.`radioComunicacao` AS boletimchecklist_radioComunicacao,
     boletimchecklist.`protecaoFrontalRadiador` AS boletimchecklist_protecaoFrontalRadiador,
     boletimchecklist.`presencaGeralTrincasMaquinaBase` AS boletimchecklist_presencaGeralTrincasMaquinaBase,
     boletimchecklist.`pinosBielasFacasRolosECilindros` AS boletimchecklist_pinosBielasFacasRolosECilindros,
     boletimchecklist.`presencaGeralTrincasAcessorio` AS boletimchecklist_presencaGeralTrincasAcessorio,
     boletimchecklist.`paradaImediata` AS boletimchecklist_paradaImediata,
     boletimchecklist.`paradaCombinada` AS boletimchecklist_paradaCombinada,
     boletimchecklist.`parabrisaFrontal` AS boletimchecklist_parabrisaFrontal,
     boletimapontamento.`id` AS boletimapontamento_id,
     boletimapontamento.`checkListId` AS boletimapontamento_checkListId,
     boletimapontamento.`codigoAtividade` AS boletimapontamento_codigoAtividade,
     boletimapontamento.`cto` AS boletimapontamento_cto,
     boletimapontamento.`dataHoraCadastro` AS boletimapontamento_dataHoraCadastro,
     boletimapontamento.`dataHoraUpload` AS boletimapontamento_dataHoraUpload,
     boletimapontamento.`horimetro` AS boletimapontamento_horimetro,
     boletimapontamento.`idFirebase` AS boletimapontamento_idFirebase,
     boletimapontamento.`mecanicoId` AS boletimapontamento_mecanicoId,
     boletimapontamento.`nomeAtividade` AS boletimapontamento_nomeAtividade,
     boletimapontamento.`numeroLaudo` AS boletimapontamento_numeroLaudo,
     boletimapontamento.`odometro` AS boletimapontamento_odometro,
     boletimapontamento.`producao` AS boletimapontamento_producao,
     boletimapontamento.`responsavel` AS boletimapontamento_responsavel,
     boletimapontamento.`talhao` AS boletimapontamento_talhao,
     boletimapontamento.`boletimDiario_id` AS boletimapontamento_boletimDiario_id
FROM
    `boletimdiario` boletimdiario
     left JOIN `boletimchecklist` boletimchecklist  ON boletimchecklist.`id` = boletimdiario.`CHECK_LIST_ID`
     left JOIN `boletimapontamento` boletimapontamento ON boletimdiario.`id` = boletimapontamento.`boletimDiario_id`          
where boletimdiario.id =$P{idBoletimdiario}