<template>
	<svws-ui-app-layout :fullwidth-content="true">
		<template #main>
			<div class="init-wrapper">
				<div class="init-container">
					<div class="init-form modal modal--sm">
						<div class="modal--content-wrapper">
							<div class="modal--content">
								<div class="init-form-header mb-8 px-8 py-4 mt-6">
									<h1 class="leading-none text-center w-full">
										<span class="font-normal">Import Schild 2-Datenbank</span>
									</h1>
								</div>
								<svws-ui-content-card title="MDB-Datei auswählen">
									<div class="content-wrapper">
										<svws-ui-text-input v-model="password" type="password" placeholder="Passwort" />
										<input type="file" accept=".mdb" @change="import_file" :disabled="loading">
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
							</div>
						</div>
					</div>
				</div>
			</div>
		</template>
	</svws-ui-app-layout>
</template>

<script setup lang="ts">

	import type { InitSchild2Props } from "./SInitSchild2Props";
	import {ref} from "vue";

	const props = defineProps<InitSchild2Props>();

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
		const formData = new FormData();
		formData.append("data", file);
		formData.append("password", password.value)
		status.value = await props.migrateMDB(formData);
		loading.value = false;
	}
</script>

<style lang="postcss" scoped>
.init-wrapper {
	@apply flex h-full flex-col justify-between;
}

.init-container {
	@apply bg-cover bg-top rounded-t-2xl h-full flex flex-col justify-center items-center px-4;
	/*background-image: radial-gradient(rgba(0,0,0,0.25), rgba(0,0,0,0.3)), url("/images/init-background-1.jpg");*/
	/*background-image: radial-gradient(rgba(0,0,0,0.15), rgba(0,0,0,0.2)), url("/images/placeholder-background.jpg");*/
	background-image: url("/images/placeholder-background.jpg");
}

.modal {
	@apply shadow-black/25 rounded-xl;
}

.init-form .modal--content {
	@apply p-0;
}

.init-form-header {
	@apply flex flex-row items-start justify-between gap-4 font-bold leading-tight;
	font-size: 2.618rem;
}
</style>
