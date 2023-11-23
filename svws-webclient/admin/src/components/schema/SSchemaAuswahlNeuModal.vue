<template>
	<slot :open-modal="openModal" />
	<svws-ui-modal :show="showModal">
		<template #modalTitle>Neues Schema anlegen</template>
		<template #modalContent>
			<div class="flex justify-center flex-wrap items-center gap-1">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-text-input v-model="schema" required placeholder="Schemaname" />
					<template v-if="loading">
						<div class="flex">
							<svws-ui-spinner :spinning="true" /> Das Schema wird angelegt…
						</div>
					</template>
					<svws-ui-spacing />
					<svws-ui-text-input v-model="user" required placeholder="Benutzername" />
					<svws-ui-text-input v-model="password" required placeholder="Passwort" />
				</svws-ui-input-wrapper>
			</div>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="showModal().value = false" :disabled="loading"> Abbrechen </svws-ui-button>
			<svws-ui-button type="secondary" @click="add" :disabled="schema.length === 0 || user.length === 0 || loading"> Schema anlegen </svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import type { SimpleOperationResponse } from "@core";
	import { BenutzerKennwort } from "@core";
	import { ref } from "vue";

	const props = defineProps<{
		addSchema:  (data: BenutzerKennwort, schema: string) => Promise<SimpleOperationResponse>;
	}>();

	const schema = ref<string>('');
	const user = ref<string>('');
	const password = ref<string>('');
	const loading = ref<boolean>(false);

	const _showModal = ref<boolean>(false);
	const showModal = () => _showModal;

	const openModal = () => {
		showModal().value = true;
	}

	async function add() {
		const data = new BenutzerKennwort();
		data.user = user.value;
		data.password = password.value;
		loading.value = true;
		await props.addSchema(data, schema.value);
		schema.value = '';
		user.value = '';
		password.value = '';
		showModal().value = false;
		loading.value = false;
	}
</script>
