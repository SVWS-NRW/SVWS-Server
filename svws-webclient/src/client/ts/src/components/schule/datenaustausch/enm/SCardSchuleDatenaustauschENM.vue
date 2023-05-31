<template>
	<svws-ui-content-card title="ENM-Datei">
		<div class="flex items-start gap-3">
			<svws-ui-text-input v-model="password" type="password" placeholder="Passwort" />
			<svws-ui-text-input v-model="salt" type="password" placeholder="Salt" />
			<input type="file" accept=".base64" @change="import_file" :disabled="loading">
			<svws-ui-spinner :spinning="loading" />
			<br>{{
				status === false
					? "Fehler beim Upload"
					: status === true
						? "Upload erfolgreich"
						: ""
			}}
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">
	import { ref } from "vue";

	const props = defineProps<{
		setImportENM: (file: File, password: string, salt: string) => Promise<boolean>;
	}>();

	const status = ref<boolean | undefined>(undefined);
	const loading = ref<boolean>(false);
	const password = ref<string>("");
	const salt = ref<string>("");

	async function import_file(event: Event) {
		const target = event.target as HTMLInputElement;
		if (!target.files?.length)
			return;
		const file = target.files.item(0);
		if (!file)
			return;
		loading.value = true;
		try {
			status.value = await props.setImportENM(file, password.value, salt.value);
			loading.value = false;
		} catch (e) {
			loading.value = false;
			throw e as Error;
		}
	}

	defineExpose({
		status
	});
</script>
