<template>
	<div class="flex flex-col gap-2 m-4">
		<div class="flex flex-col gap-2">
			<svws-ui-text-input v-model.trim="user" required placeholder="Benutzername" :disabled="loading" />
			<svws-ui-text-input v-model.trim="password" required placeholder="Passwort" :disabled="loading" type="password" />
		</div>
		<div class="mt-2">
			<svws-ui-button type="secondary" @click="add" :disabled="(props.schema === undefined) || user.length === 0 || loading">
				<svws-ui-spinner :spinning="loading" />
				Hinzufügen
			</svws-ui-button>
		</div>
		<log-box :logs="logs" :status="status">
			<template #button>
				<svws-ui-button v-if="status !== undefined" type="transparent" @click="clear" title="Log verwerfen"> Log verwerfen </svws-ui-button>
			</template>
		</log-box>
	</div>
</template>

<script setup lang="ts">

	import { ref } from "vue";
	import { BenutzerKennwort, SimpleOperationResponse, type List } from "@core";

	const props = defineProps<{
		addExisting: ((data: BenutzerKennwort, schema: string) => Promise<void>);
		schema: string;
	}>();

	const user = ref<string>('');
	const password = ref<string>('');
	const loading = ref<boolean>(false);
	const logs = ref<List<string|null>>();
	const status = ref<boolean>();

	async function add() {
		loading.value = true;
		const data = new BenutzerKennwort();
		data.user = user.value;
		data.password = password.value;
		let result = new SimpleOperationResponse();
		result.success = true;
		await props.addExisting(data, props.schema);
		user.value = '';
		password.value = '';
		loading.value = false;
		if (result.success)
			clear();
	}

	function clear() {
		logs.value = undefined;
		status.value = undefined;
		loading.value = false;
		user.value = '';
		password.value = '';
	}

</script>
