<template>
	<ui-card icon="i-ri-add-line" title="Leeres Schema" subtitle="Es wird ein leeres neues Schema in der neuesten Revision erzeugt. Dieses kann im Anschluss initialisiert werden." :is-open @update:is-open="(open) => emit('opened', open)">
		<div class="input-wrapper mt-2">
			Der Name des Schemas darf nur Buchstaben (ohne Umlaute), Zahlen sowie die Zeichen "$" und "_" enthalten:
			<svws-ui-text-input v-model.trim="schemaname" required placeholder="Schemaname" :valid="validatorSchemaName" :disabled="loading" />
			<svws-ui-spacing />
			<svws-ui-text-input v-model.trim="user" required placeholder="Benutzername" :disabled="loading" :valid="validatorUsername" />
			<svws-ui-text-input v-model.trim="password" required placeholder="Passwort" :disabled="loading" type="password" />
		</div>
		<template #buttonFooterLeft>
			<svws-ui-button :disabled="(schemaname.length === 0) || (user.length === 0) || (password.length === 0) || (user === 'root') || loading" title="Schema anlegen" @click="actionFunction" :is-loading="loading" class="mt-4">
				<svws-ui-spinner v-if="loading" spinning />
				<span v-else class="icon i-ri-play-line" />
				Schema anlegen
			</svws-ui-button>
		</template>
	</ui-card>
</template>

<script setup lang="ts">

	import { ref } from "vue";
	import { BenutzerKennwort } from "@core/core/data/BenutzerKennwort";
	import { SimpleOperationResponse } from "@core/core/data/SimpleOperationResponse";
	import type { List } from "@core/java/util/List";
	import { validatorSchemaName } from "~/utils/helfer";

	const props = defineProps<{
		addSchema: ((data: BenutzerKennwort, schema: string) => Promise<SimpleOperationResponse>);
		setStatus: (loading: boolean, status?: boolean, logs?: List<string | null>) => void;
		loading: boolean;
		validatorUsername: (username: string | null) => boolean;
		isOpen: boolean;
	}>();

	const emit = defineEmits<{
		'opened': [value: boolean];
	}>();

	const schemaname = ref<string>('');
	const user = ref<string>('');
	const password = ref<string>('');

	async function actionFunction() {
		props.setStatus(true);
		const data = new BenutzerKennwort();
		data.user = user.value;
		data.password = password.value;
		let result = new SimpleOperationResponse();
		result = await props.addSchema(data, schemaname.value);
		schemaname.value = '';
		user.value = '';
		password.value = '';
		props.setStatus(false, result.success, result.log);
	}

</script>
