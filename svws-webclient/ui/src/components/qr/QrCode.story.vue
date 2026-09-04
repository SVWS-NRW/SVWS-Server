<template>
	<Story title="QR-Code" id="Qr-Code" icon="ri:qr-code-line" :layout="{type: 'grid', width: '45%'}">
		<Variant id="totp"	title="TOTP">
			<qr-code :uri="`otpauth://totp/${issuerEnc}:${accountEnc}?secret=${secretEnc}&issuer=${issuerEnc}`" />
			<div class="font-mono text-headline-md text-center mt-4"> {{ `otpauth://totp/${issuerEnc}:${accountEnc}?secret=${secretEnc}&issuer=${issuerEnc}` }} </div>
			<template #source>
				{{ `
<qr-code
	:uri="otpauth://totp/${issuerEnc}:${accountEnc}?secret=${secretEnc}&issuer=${issuerEnc}" />
        ` }}
			</template>
		</Variant>
		<Variant id="uri" title="URI">
			<qr-code :uri />
			<div class="font-mono text-headline-md text-center mt-4"> {{ uri }} </div>
			<template #source>
				{{ `
<qr-code
	:uri="${uri}" />
        ` }}
			</template>
		</Variant>
		<template #controls>
			<HstText v-model="issuerEnc" title="Issuer" />
			<HstText v-model="accountEnc" title="Account" />
			<HstText v-model="secretEnc" title="Secret" />
			<hr>
			<HstText v-model="uri" title="URI" />
		</template>
		<template #docs>
			<div class="text-headline-md">QR-Code</div>
			Diese Komponente ermöglicht die Darstellung von QR-Codes. Als einziges Prop wird <emph>:uri</emph> verwendet.
			Es wird ein String oder eine URL übergeben, die anschließend gerendert wird.
		</template>
	</Story>
</template>

<script setup lang="ts">
	import { ref } from 'vue';

	const issuerEnc = ref("issuer");
	const accountEnc = ref("account");
	const secretEnc = ref("secret");
	const uri = ref(`https://github.com/SVWS-NRW/SVWS-Server`);

</script>
