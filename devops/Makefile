# make upgrade arg1=portal-test-eureka arg2=test-eureka
upgrade:
	helm upgrade --kubeconfig ./portal/.kube/config-bj-dev \
	$(arg1) \
	-n $(arg1) \
	--install --create-namespace \
	--set global.gateway.version=test-eureka \
	--set global.eureka.host=http://eureka.infrastruction-test-eureka.svc.cluster.local \
	--set portal_server.version0=$(arg2) ./portal
push:
	helm push --version=0.0.62 ./portal/ quantex
dry:
	helm install --kubeconfig ./portal/.kube/config portal-test --dry-run ./portal/ 
test:
	helm get all --kubeconfig ./portal/.kube/config portal-test

dependency:
	cd portal && helm dependency update

info:
	kubectl --kubeconfig ./portal/.kube/config  get namespaces
list-pod:
	kubectl --kubeconfig ./portal/.kube/config -n $(arg1) get pod; \
	kubectl --kubeconfig ./portal/.kube/config -n $(arg1) get ing
	
drop-pod:
	kubectl --kubeconfig ./portal/.kube/config -n $(arg1) delete ing,deployment --selector="app.name=portal-gateway" ;\
	kubectl --kubeconfig ./portal/.kube/config -n $(arg1) delete ing --all

ns=portal-feat-41
kube=kubectl --kubeconfig ./portal/.kube/config -n $(ns)
sql="update portal_oauth_client_details set web_server_redirect_uri='http://portal-gateway.feat-41.bj.iquantex.com/login/oauth2/code/gateway' where id = '1';"
sql2="select id,web_server_redirect_uri from portal_oauth_client_details;"
mysql_shell=$(kube) exec $$($(kube) get pod -l app=mysql -o jsonpath='{.items[0].metadata.name}') -c mysql -- mysql -u root -phuang
fix:
	$(mysql_shell) -D portal_server -Be $(sql)
	$(mysql_shell) -D portal_server -Be $(sql2)
mysql:
	$(mysql_shell) 

sql_code="select * from oauth_code;"
code:
	$(mysql_shell) -D auth_server -Be $(sql_code)

