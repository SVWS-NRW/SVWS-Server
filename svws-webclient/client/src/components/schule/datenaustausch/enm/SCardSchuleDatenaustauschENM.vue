<template>
	<svws-ui-content-card title="Datei aus dem Externen Notenmanager hochladen">
		<svws-ui-input-wrapper :grid="2">
			<svws-ui-text-input v-model.trim="password" type="password" placeholder="Passwort" />
			<svws-ui-text-input v-model.trim="salt" type="password" placeholder="Salt" />
			<svws-ui-spacing />
			<div class="col-span-full">
				<input class="contentFocusField" type="file" accept=".base64" @change="import_file" :disabled="loading">
				<svws-ui-spinner :spinning="loading" />
			</div>
			<br>{{
				status === false
					? "Fehler beim Upload"
					: status === true
						? "Upload erfolgreich"
						: ""
			}}
		</svws-ui-input-wrapper>
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
		if ((target.files === null) || (target.files.length === 0))
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
			throw e;
		}
	}

	defineExpose({
		status
	});
</script>
