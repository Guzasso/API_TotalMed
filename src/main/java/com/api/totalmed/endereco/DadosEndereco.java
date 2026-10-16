package com.api.totalmed.endereco;


public record DadosEndereco(String logradouro,
                           String bairro,
                           String cep,
                           String cidade,
                           String  estado,
                           String complemento,
                           String uf,
                           String numero) {
}
