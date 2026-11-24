import test from 'node:test';
import assert from 'node:assert/strict';
import { listFrom, parseRole, ubsId, ubsName, serviceId, serviceName, statusLabel } from '../src/utils/normalize.js';

test('extrai coleções reais das respostas do backend',()=>{
 assert.deepEqual(listFrom({usuarios:[{id:1}]},['usuarios']),[{id:1}]);
 assert.deepEqual(listFrom({unidadeSaude:[]},['unidadeSaude']),[]);
 assert.deepEqual(listFrom(null,['usuarios']),[]);
});
test('mantém CNES como string, inclusive zeros à esquerda',()=>{
 assert.equal(ubsId({cnes:'0012345'}),'0012345');
 assert.equal(ubsName({nome:'UBS Central'}),'UBS Central');
 assert.equal(serviceId({id:31}),'31');
 assert.equal(serviceName({descricao:'Clínica geral'}),'Clínica geral');
});
test('reconhece perfis sem conceder supervisor por padrão',()=>{
 assert.equal(parseRole({nivelAcesso:{descricao:'Supervisor'}}),'supervisor');
 assert.equal(parseRole({perfil:'Atendente'}),'atendente');
 assert.equal(parseRole({perfil:'Paciente'}),'paciente');
 assert.equal(parseRole({perfil:'Não identificado'}),'paciente');
});
test('padroniza apresentação de status',()=>{
 assert.equal(statusLabel('PENDENTE'),'Pendente');
 assert.equal(statusLabel('CONFIRMADO'),'Confirmado');
});
