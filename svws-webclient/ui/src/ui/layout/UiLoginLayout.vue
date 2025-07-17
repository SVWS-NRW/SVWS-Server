<template>
	<div class="h-screen w-screen overflow-hidden bg-ui">
		<div class="h-full w-full overflow-hidden flex flex-col justify-center items-center bg-top bg-cover bg-[url(@images/bglogin.jpg)]">
			<div class="h-fit max-h-full w-full overflow-hidden m-auto flex flex-col items-center bg-ui text-ui border border-ui rounded-xl" :class="classSize">
				<span v-if="underConstruction" class="h-[1rem] w-full" style="background: repeating-linear-gradient( -45deg, #000, #000 10px, #ffff00 10px, #ffff00 20px );" />
				<div class="p-4 w-full h-full overflow-hidden text-center flex flex-col gap-4">
					<!-- Header -->
					<div v-if="!hideHeader" class="w-full flex flex-col gap-2">
						<h1 class="text-headline-xl">SVWS NRW</h1>
						<h2 class="text-headline-sm opacity-50">
							Schulverwaltung für<br>Nordrhein-Westfalen
						</h2>
						<h2 v-if="application !== undefined" class="text-headline-sm leading-none w-full">
							{{ application }}
						</h2>
					</div>
					<!-- Hauptbereich des Login (Slot) -->
					<div class="w-full min-h-fit overflow-y-auto flex flex-col px-1" :class="{ 'pb-4': !hideHinweis && (version === undefined), 'pt-4': !hideHeader }">
						<slot name="main" />
					</div>
					<!-- Footer: Version, Impressum und Datenschutz -->
					<div v-if="version !== undefined" class="text-sm font-medium px-1">
						<div class="flex gap-2 items-center opacity-50">
							<slot name="logo" />
							<div class="text-left flex flex-col leading-5">
								<span class="font-bold">SVWS-NRW</span>
								<div class="flex gap-2 place-items-center">
									<div>
										<span class="font-bold">v{{ version }}</span>
										<span v-if="version.includes('SNAPSHOT')">
											<a :href="`https://github.com/SVWS-NRW/SVWS-Server/commit/${githash}`"> &nbsp;{{ githash.substring(0, 8) }} </a>
										</span>
									</div>
									<div @click="copyToClipboard" class="cursor-pointer place-items-center flex">
										<span class="icon-sm i-ri-file-copy-line" v-if="copied === null" />
										<span class="icon-sm i-ri-error-warning-fill" v-else-if="copied === false" />
										<span class="icon-sm i-ri-check-line icon-ui-brand" v-else />
									</div>
								</div>
								<nav class="flex items-center gap-2">
									<a class="login-footer-link" href="#">Impressum</a>
									<datenschutz-modal v-slot="{ openModal }">
										<a class="login-footer-link" href="#" @click="openModal()">Datenschutz</a>
									</datenschutz-modal>
								</nav>
							</div>
						</div>
					</div>
					<!-- Footer: Hinweis zur Lesbarkeit -->
					<p v-if="!hideHinweis" class="opacity-50 text-sm text-left px-1">
						Hinweis: Um eine gute Lesbarkeit zu erzeugen, wird bei SVWS-NRW möglichst auf
						geschlechtsneutrale Begriffe wie Lehrkräfte, Klassenleitung, Erziehungsberechtigte usw.
						zurückgegriffen. An Stellen, wo das nicht möglich ist, wird versucht alle
						Geschlechter gleichermaßen zu berücksichtigen.
					</p>
				</div>
			</div>
		</div>
	</div>
</template>

<script setup lang='ts'>

	import { computed, ref } from 'vue';

	const props = withDefaults(defineProps<{
		version?: string;
		githash?: string;
		size?: 'sm' | 'md' | 'lg';
		application?: string;
		hideHeader?: boolean;
		hideHinweis?: boolean;
		underConstruction?: boolean;
	}>(), {
		version: undefined,
		githash: undefined,
		size: 'sm',
		application: undefined,
		hideHeader: false,
		hideHinweis: false,
		underConstruction: false,
	});

	const copied = ref<boolean|null>(null);

	async function copyToClipboard() {
		try {
			await navigator.clipboard.writeText(`${props.version} ${props.githash}`);
		} catch(e) {
			copied.value = false;
		}
		copied.value = true;
	}

	const classSize = computed<string>(() => {
		switch (props.size) {
			case 'sm': return 'modal--sm';
			case 'md': return 'modal--md';
			case 'lg': return 'modal--lg';
			default: return '';
		}
	});

</script>


<style scoped>

	.login-footer-link {
		display: inline-block;
	}

	.login-footer-link:hover,
	.login-footer-link:focus,
	.login-footer-link:hover .hover-underline,
	.login-footer-link:focus .hover-underline {
		text-decoration-line: underline;
	}

</style>
