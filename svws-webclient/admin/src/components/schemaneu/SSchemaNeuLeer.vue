<template>
	<svws-ui-action-button title="Leeres Schema" description="Es wird ein leeres neues Schema in der neuesten Revision erzeugt. Dieses kann im Anschluss initialisiert werden" icon="i-ri-add-line" :action-function
		:action-disabled="(schemaname.length === 0) || (user.length === 0) || (password.length === 0) || (user === 'root') || loadingFunction().value" :is-loading="loadingFunction().value" action-label="Schema anlegen" :is-active>
		<div class="input-wrapper">
			<svws-ui-text-input v-model.trim="schemaname" required placeholder="Schemaname" :disabled="loadingFunction().value" />
			<svws-ui-spacing />
			<svws-ui-text-input v-model.trim="user" required placeholder="Benutzername" :disabled="loadingFunction().value" :valid="validatorUsername" />
			<svws-ui-text-input v-model.trim="password" required placeholder="Passwort" :disabled="loadingFunction().value" type="password" />
		</div>
	</svws-ui-action-button>
</template>

<script setup lang="ts">

	import { type ShallowRef, ref } from "vue";
	import { BenutzerKennwort } from "@core/core/data/BenutzerKennwort";
	import { SimpleOperationResponse } from "@core/core/data/SimpleOperationResponse";
	import type { List } from "@core/java/util/List";

	const props = defineProps<{
		addSchema: ((data: BenutzerKennwort, schema: string) => Promise<SimpleOperationResponse>);
		logsFunction: () => ShallowRef<List<string | null> | undefined>;
		statusFunction: () => ShallowRef<boolean | undefined>;
		loadingFunction: () => ShallowRef<boolean>;
		validatorUsername: (username: string | null) => boolean;
		isActive: boolean;
	}>();

	const schemaname = ref<string>('');
	const user = ref<string>('');
	const password = ref<string>('');

	async function actionFunction() {
		props.loadingFunction().value = true;
		const data = new BenutzerKennwort();
		data.user = user.value;
		data.password = password.value;
		let result = new SimpleOperationResponse();
		result = await props.addSchema(data, schemaname.value);
		props.logsFunction().value = result.log;
		props.statusFunction().value = result.success;
		schemaname.value = '';
		user.value = '';
		password.value = '';
		props.loadingFunction().value = false;
	}

</script>
