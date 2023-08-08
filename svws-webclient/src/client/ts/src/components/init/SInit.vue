<template>
	<svws-ui-app-layout :fullwidth-content="true">
		<template #main>
			<div class="init-wrapper">
				<div class="init-container">
					<div class="init-form modal modal--md">
						<div class="modal--content-wrapper">
							<div class="modal--content">
								<div class="init-form-header mb-8 px-8 py-4 mt-6">
									<h1 class="leading-none text-center w-full">
										<span class="font-normal">Initialisierung der Datenbank</span>
									</h1>
								</div>
								<div class="w-full mt-1 px-8">
									Die Datenbank soll initialisiert werden mit einer Auswahl aus dem Schulkatalog, der Migration einer
									bestehenden Schild 2-Datenbank oder aus einem Backup einer SVWS-Datenbank.
								</div>
								<div class="flex flex-row w-full mt-3 mb-3 px-8 gap-3">
									<div class="flex flex-col gap-2 basis-1/4">
										<div class="border rounded-md px-2 hover:bg-slate-100 cursor-pointer" @click="setSource('schulkatalog')">
											<h3>Schulkatalog</h3>
											Daten werden über die Auswahl der Schulnummer ausgwählt
										</div>
										<div class="border rounded-md px-2 hover:bg-slate-100 cursor-pointer" @click="setSource('schild2')">
											<h3>Schild 2-Datenbank</h3>
											Daten werden über die Auswahl einer existierenden Schild 2-Datenbank importiert.
										</div>
										<div class="border rounded-md px-2 hover:bg-slate-100 cursor-pointer" @click="setSource('backup')">
											<h3>Backup</h3>
											Daten werden aus einem Backup wiederhergestellt
										</div>
									</div>
									<s-init-schulkatalog v-if="source === 'schulkatalog'" :list-schulkatalog="listSchulkatalog" :init-schule="initSchule" class="border rounded-md px-8 pb-3" />
									<s-init-schild2 v-if="source === 'schild2'" :migrate-d-b="migrateDB" :set-d-b="setDB" class="border rounded-md px-8 pb-3" :db="db" />
									<s-init-backup v-if="source === 'backup'" :migrate-d-b="migrateDB" class="border rounded-md px-8 pb-3" />
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</template>
	</svws-ui-app-layout>
</template>

<script setup lang="ts">

	import { ref } from "vue";
	import type { InitProps } from "./SInitProps";

	const inputFocus = ref(false);

	const props = defineProps<InitProps>()
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

.init-form-header {
	@apply flex flex-row items-start justify-between gap-4 font-bold leading-tight;
	font-size: 2.618rem;
}
.init-form .modal--content {
	@apply p-0;
}
</style>
