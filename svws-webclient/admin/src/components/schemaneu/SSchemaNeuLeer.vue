<template>
	<ui-card icon="i-ri-add-line" title="Leeres Schema" subtitle="Es wird ein leeres neues Schema in der neuesten Revision erzeugt. Dieses kann im Anschluss initialisiert werden." :is-open @update:is-open="(open) => emit('opened', open)">
		<div class="input-wrapper mt-2">
			<svws-ui-text-input v-model.trim="schemaname" required placeholder="Schemaname" :disabled="loadingFunction().value" />
			<svws-ui-spacing />
			<svws-ui-text-input v-model.trim="user" required placeholder="Benutzername" :disabled="loadingFunction().value" :valid="validatorUsername" />
			<svws-ui-text-input v-model.trim="password" required placeholder="Passwort" :disabled="loadingFunction().value" type="password" />
		</div>
		<template #buttonFooterLeft>
			<svws-ui-button :disabled="(schemaname.length === 0) || (user.length === 0) || (password.length === 0) || (user === 'root') || loadingFunction().value" title="Schema anlegen" @click="actionFunction" :is-loading="loadingFunction().value" class="mt-4">
				<svws-ui-spinner v-if="loadingFunction().value" spinning />
				<span v-else class="icon i-ri-play-line" />
				Schema anlegen
			</svws-ui-button>
		</template>
	</ui-card>
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
		isOpen: boolean;
	}>();

	const emit = defineEmits<{
		'opened': [value: boolean];
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
