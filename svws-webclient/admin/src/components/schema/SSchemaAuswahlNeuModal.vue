<template>
	<slot :open-modal="openModal" />
	<svws-ui-modal :show="showModal" size="big">
		<template #modalTitle>{{ title }}</template>
		<template #modalContent>
			<svws-ui-input-wrapper>
				<svws-ui-text-input v-if="istModalNeuesSchema" v-model="schemaname" required placeholder="Schemaname" :disabled="loading" />
				<div v-else>
					Schemaname: {{ props.schema }}
				</div>
				<svws-ui-spacing />
				<svws-ui-text-input v-model="user" required placeholder="Benutzername" :disabled="loading" />
				<svws-ui-text-input v-model="password" required placeholder="Passwort" :disabled="loading" />
			</svws-ui-input-wrapper>
		</template>
		<template #modalActions>
			<template v-if="status === undefined">
				<svws-ui-button type="secondary" @click="add" :disabled="(istModalNeuesSchema && schemaname.length === 0) || (!istModalNeuesSchema && props.schema === undefined) || user.length === 0 || loading">
					<svws-ui-spinner :spinning="loading" />
					{{ okButtonText }}
				</svws-ui-button>
				<svws-ui-button type="secondary" @click="close" :disabled="loading"> Abbrechen </svws-ui-button>
			</template>
			<template v-else>
				<svws-ui-button type="secondary" @click="close"> Schließen </svws-ui-button>
			</template>
		</template>
		<template #modalLogs>
			<log-box :logs="logs" :status="status" />
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import { computed, ref } from "vue";
	import { BenutzerKennwort, SimpleOperationResponse, type List } from "@core";

	const props = defineProps<{
		addSchema?: ((data: BenutzerKennwort, schema: string) => Promise<SimpleOperationResponse>);
		addExisting?: ((data: BenutzerKennwort, schema: string) => Promise<void>);
		schema?: string;
	}>();

	const schemaname = ref<string>('');
	const user = ref<string>('');
	const password = ref<string>('');
	const loading = ref<boolean>(false);
	const logs = ref<List<string|null>>();
	const status = ref<boolean>();

	const _showModal = ref<boolean>(false);
	const showModal = () => _showModal;

	const istModalNeuesSchema = computed<boolean>(() => props.addSchema !== undefined);

	const title = computed<string>(() => istModalNeuesSchema.value ? "Neues Schema anlegen" : "Schema in Konfiguration übernehmen");
	const okButtonText = computed<string>(() => istModalNeuesSchema.value ? "Schema anlegen" : "Übernehmen");

	const openModal = () => {
		showModal().value = true;
	}

	async function add() {
		loading.value = true;
		const data = new BenutzerKennwort();
		data.user = user.value;
		data.password = password.value;
		let result = new SimpleOperationResponse();
		if (istModalNeuesSchema.value && (props.addSchema !== undefined)) {
			result = await props.addSchema(data, schemaname.value);
			logs.value = result.log;
			status.value = result.success;
			schemaname.value = '';
		} else if ((props.addExisting !== undefined) && (props.schema !== undefined)) {
			result.success = true;
			await props.addExisting(data, props.schema);
		}
		user.value = '';
		password.value = '';
		loading.value = false;
		if (result.success)
			close();
	}

	function close() {
		showModal().value = false;
		logs.value = undefined;
		status.value = undefined;
		user.value = '';
		password.value = '';
	}

</script>
